import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileExtractor {
    static String savePath ="";
    static File file;

    public FileExtractor(String savePath) {
        this.savePath = savePath;
        file = new File(savePath);
    }

    private  List<File> ergodic(File file, List<File> resultFileName){
        File[] files = file.listFiles();
        if(files==null) {
            return resultFileName;// 判断目录下是不是空的
        }
        for (File f : files) {

            if(f.isDirectory()){// 判断是否文件夹
                ergodic(f,resultFileName);// 调用自身,查找子目录
            }else
                resultFileName.add(f);
        }
        return resultFileName;
    }
    public List<File> getExtractedFiles(){
        List<File> res = new ArrayList<>();
        return ergodic(file, res);
    }
    private  void copyFileUsingJava7Files(File source, File dest)throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
    void extractfile() throws IOException {
        try {
            List<File> res = getExtractedFiles();
            for (File x : res) {
                copyFileUsingJava7Files(x, new File(savePath + "\\" + x.getPath().substring(x.getPath().lastIndexOf("\\") + 1)));
            }
        }catch (Exception e){
            throw e;
        }

    }


}
