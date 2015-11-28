package org.black.kotlin.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import org.black.kotlin.project.KotlinProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.load.kotlin.PackageClassUtils;
import org.jetbrains.kotlin.name.FqName;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 *
 * @author Александр
 */
public class ProjectUtils {

    private static final String LIB_FOLDER = "lib";
    private static final String LIB_EXTENSION = "jar";
    public static final String KT_HOME = System.getenv("KT_HOME") + "\\";

    private org.netbeans.api.project.ProjectUtils projUtils;

    public static String findMain(FileObject[] files) throws FileNotFoundException, IOException {
        for (FileObject file : files) {
            if (!file.isFolder()) {
                for (String line : file.asLines()) {
                    if (line.contains("fun main(")) {
                        return file.getPath();
                    }
                }
            } else {
                String main = findMain(file.getChildren());
                if (main != null) {
                    return main;
                }
            }
        }

        return null;
    }

    public static String getOutputDir(KotlinProject proj) {
        
        File path = new File(proj.getProjectDirectory().getPath() + "/build");

        if (!path.exists()) {
            path.mkdirs();
        }

        String dir = proj.getProjectDirectory().getPath() + "/build";
        String[] dirs = dir.split("/");
        StringBuilder outputDir = new StringBuilder("");

        for (String str : dirs) {
            outputDir.append(str);
            outputDir.append("\\");
        }
        outputDir.append(proj.getProjectDirectory().getName()).append(".jar");

        return outputDir.toString();
    }

    public static void clean(KotlinProject proj) {

        try {
            if (proj.getProjectDirectory().getFileObject("build") != null) {
                proj.getProjectDirectory().getFileObject("build").delete();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    @NotNull
    public static List<String> getClasspath() {
        List<String> classpath = new ArrayList(KotlinClasspath.getKotlinClasspath());

//        for (String s: classpath){
//            DialogDisplayer.getDefault().notify(new NotifyDescriptor.
//                Message(s));
//        }
        return classpath;
    }

    

    @Nullable
    public static String getPackageByFile(FileObject file) {
//        JetFile jetFile = new JetFile(file,true);// = KotlinPsiManager.INSTANCE.getParsedFile(file);
//        
//        assert jetFile != null;
//        
//        return jetFile.getPackageFqName().asString();
        return "stub";
    }

    public static FqName createPackageClassName(FileObject file) {
        String filePackage = getPackageByFile(file);
        if (filePackage == null) {
            return null;
        }
        return PackageClassUtils.getPackageClassFqName(new FqName(filePackage));
    }

    public static String buildLibPath(String libName) {
        return KT_HOME + buildLibName(libName);
    }

    private static String buildLibName(String libName) {
        return LIB_FOLDER + "/" + libName + "." + LIB_EXTENSION;
    }

}