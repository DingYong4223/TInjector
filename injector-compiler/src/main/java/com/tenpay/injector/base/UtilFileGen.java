package com.tenpay.injector.base;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;

/**
 * Utils used to write a [JavaFile] to a file.
 * delanding
 */
public class UtilFileGen {

    /**
     * Writes a Java file to the file system after deleting the previous copy.
     * @param file  the file to write.
     * @throws IOException throws an exception if we are unable to write the file to the filesystem.
     */
    public static void writeToDisk(JavaFile file, Filer filer) {
        String fileName = file.packageName + "." + file.typeSpec.name;
        List<Element> originatingElements = file.typeSpec.originatingElements;
        try {
            Element[] args = new Element[originatingElements.size()];
            originatingElements.toArray(args);
            FileObject sfo = filer.createSourceFile(fileName, args);

            sfo.delete();
            Writer writer = sfo.openWriter();
            try {
                file.writeTo(writer);
            } finally {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
