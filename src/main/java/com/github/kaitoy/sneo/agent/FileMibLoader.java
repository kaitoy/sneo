/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

final class FileMibLoader {

  private static final LogAdapter logger
    = LogFactory.getLogger(FileMibLoader.class.getPackage().getName());

  private final SneoVariableTextFormat format;

  public FileMibLoader(SneoVariableTextFormat format) {
    if (format == null) {
      throw new NullPointerException("format");
    }
    this.format = format;
  }

  public
  List<VariableBinding> load(String fileMibPath, OID root)
  throws FileNotFoundException, IOException {
    synchronized (format) {
      FileReader fileMibReader = null;
      BufferedReader fileMibBufferedReader = null;

      try {
        File fileMib = new File(fileMibPath);
        fileMibReader = new FileReader(fileMib);
        fileMibBufferedReader = new BufferedReader(fileMibReader);

        String line;
        List<VariableBinding> vbs = new ArrayList<VariableBinding>();

        format.init();

        while ((line = fileMibBufferedReader.readLine()) != null) {
          try {
            VariableBinding vb = format.parseVariableBinding(line);
            if (vb == null) {
              logger.info("No effective value. Ignore the line: " + line);
              continue;
            }
            if (vb.getOid().startsWith(root)) {
              vbs.add(vb);
            }
          } catch (ParseException e) {
            logger.warn("ParseException occured. Ignore the line: " + line);
          }
        }

        return vbs;
      } finally {
        if (fileMibBufferedReader != null) {
          fileMibBufferedReader.close();
        }
        if (fileMibReader != null) {
          fileMibReader.close();
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getName())
      .append("(")
      .append(format.getClass().getName())
      .append(")");
    return  sb.toString();
  }
}
