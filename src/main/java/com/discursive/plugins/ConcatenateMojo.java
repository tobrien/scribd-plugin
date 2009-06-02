package com.discursive.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This Mojo simply verifies that each chapter has an appropriate identifer.
 * 
 * @goal concatenate
 * @phase process-classes
 * @requiresProject
 */
public class ConcatenateMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * @parameter
	 */
	protected List<String> pdfs;

	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	protected File outputDir;

	/**
	 * @parameter
	 * @required
	 */
	protected String outputFilename;
	

	public void execute() throws MojoExecutionException, MojoFailureException {

        try {

            int pageOffset = 0;

            ArrayList master = new ArrayList();

            int f = 0;

            File outfile = new File( outputDir, outputFilename );
            getLog().info( "Saving Concatenated PDF to: " + outfile.getPath() );
            
            
            Document document = null;

            PdfCopy  writer = null;

            getLog().info( "Processing PDFs" );
            for( String pdf : pdfs ) {
            	
            	getLog().info( "Processing PDF: " + pdf );
            
                // we create a reader for a certain document

                PdfReader reader = new PdfReader(new FileInputStream( pdf ) );

                reader.consolidateNamedDestinations();

                // we retrieve the total number of pages

                int n = reader.getNumberOfPages();

                List bookmarks = SimpleBookmark.getBookmark(reader);

                if (bookmarks != null) {

                    if (pageOffset != 0)

                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);

                    master.addAll(bookmarks);

                }

                pageOffset += n;

                

                if (f == 0) {

                    // step 1: creation of a document-object

                    document = new Document(reader.getPageSizeWithRotation(1));

                    // step 2: we create a writer that listens to the document

                    writer = new PdfCopy(document, new FileOutputStream(outfile));

                    // step 3: we open the document

                    document.open();

                }

                // step 4: we add content

                PdfImportedPage page;

                for (int i = 0; i < n; ) {

                    ++i;

                    page = writer.getImportedPage(reader, i);

                    writer.addPage(page);

                }

                PRAcroForm form = reader.getAcroForm();

                if (form != null)

                    writer.copyAcroForm(reader);

                f++;

            }

            if (!master.isEmpty())

                writer.setOutlines(master);

            // step 5: we close the document

            document.close();

        }

        catch(Exception e) {

            e.printStackTrace();

        }		
	}


}
