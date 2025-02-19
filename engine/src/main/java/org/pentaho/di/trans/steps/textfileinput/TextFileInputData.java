/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.di.trans.steps.textfileinput;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.compress.CompressionInputStream;
import org.pentaho.di.core.fileinput.FileInputList;
import org.pentaho.di.core.playlist.FilePlayList;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.errorhandling.FileErrorHandler;
import org.pentaho.di.trans.steps.fileinput.text.TextFileLine;

/**
 * @author Matt
 * @since 22-jan-2005
 * @deprecated replaced by implementation in the ...steps.fileinput.text package
 */
@Deprecated
public class TextFileInputData extends BaseStepData implements StepDataInterface {

  public List<TextFileLine> lineBuffer;

  public Object[] previous_row;

  public int nr_repeats;

  public int nrLinesOnPage;

  private FileInputList files;

  public HashMap<FileObject, Object[]> passThruFields;

  public Object[] currentPassThruFieldsRow;

  public int nrPassThruFields;

  public boolean isLastFile;

  public String filename;

  public int lineInFile;

  public FileObject file;

  public int filenr;

  public CompressionInputStream in;

  public InputStreamReader isr;

  public boolean doneReading;

  public int headerLinesRead;

  public int footerLinesRead;

  public int pageLinesRead;

  public boolean doneWithHeader;

  public FileErrorHandler dataErrorLineHandler;

  public FilePlayList filePlayList;

  public TextFileFilterProcessor filterProcessor;

  public RowMetaInterface outputRowMeta;

  public StringBuilder lineStringBuilder;

  public int fileFormatType;

  public int fileType;

  public RowMetaInterface convertRowMeta;

  public RowSet rowSet;

  /**
   * The separator (delimiter)
   */
  public String separator;

  public String enclosure;

  public String escapeCharacter;

  public boolean addShortFilename;
  public boolean addExtension;
  public boolean addPath;
  public boolean addSize;
  public boolean addIsHidden;
  public boolean addLastModificationDate;
  public boolean addUri;
  public boolean addRootUri;

  public String shortFilename;
  public String path;
  public String extension;
  public boolean hidden;
  public Date lastModificationDateTime;
  public String uriName;
  public String rootUriName;
  public long size;

  public EncodingType encodingType;

  public Map<String, Boolean> rejectedFiles;

  public TextFileInputData() {
    super();

    // linked list is better, as usually .remove(0) is applied to this list
    lineBuffer = new LinkedList<TextFileLine>();

    nr_repeats = 0;
    previous_row = null;
    filenr = 0;

    nrLinesOnPage = 0;

    in = null;

    filterProcessor = null;
    lineStringBuilder = new StringBuilder( 256 );

    rejectedFiles = new HashMap<String, Boolean>();
  }

  public FileInputList getFiles() {
    return files;
  }

  public void setFiles( FileInputList files ) {
    this.files = files;
  }
}
