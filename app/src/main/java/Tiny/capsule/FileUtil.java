package Tiny.capsule;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.telephony.gsm.GsmCellLocation;

import Tiny.capsule.http.DownloadRequest;
import Tiny.capsule.http.UploadRequest;
import Tiny.capsule.model.Global;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FileUtil {

    static String serverBaseUrl = Global.baseProject+"/resource/";

    public static String fileDir = "/data/data/Tiny.capsule/files/";
    public static String userDir = fileDir+"user/";
    public static String capsuleDir = fileDir+"capsule/";

    public static File createFile(String filename){

        File file=null;
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){

            file = new File(filename);
        }else {
            file = new File(filename);
        }
        System.out.println(filename);
        return file;

    }

    public static void writeFile2Disk(Response<ResponseBody> response, File file){

        long currentLength = 0;
        OutputStream os =null;

        InputStream is = response.body().byteStream();
        long totalLength =response.body().contentLength();
        System.out.println("writing file to "+file.getAbsolutePath());
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {

            os = new FileOutputStream(file);

            int len ;

            byte [] buff = new byte[1024];

            while((len=is.read(buff))!=-1){

                os.write(buff,0,len);
                currentLength+=len;
            }
            // httpCallBack.onLoading(currentLength,totalLength,true);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String getUserIconUrl(String username){
        return serverBaseUrl+"user/"+username+"/icon.jpg";
    }

    public static String getUserIconFilePath(String username){
        return userDir+username+"/icon.jpg";
    }

    public static String getContentPictureUrl(int id,long timestamp,int sequence){
        return serverBaseUrl+"capsule/"+id+"/"+timestamp+"-"+sequence+".jpg";
    }
    public static String getContentVideoUrl(int id,long timestamp,int sequence){
        return serverBaseUrl+"capsule/"+id+"/"+timestamp+"-"+sequence+".mp4";
    }

    public static String getContentPictureFilePath(int id,long timestamp,int sequence){
        return capsuleDir+id+"/"+timestamp+"-"+sequence+".jpg";
    }
    public static String getContentVideoFilePath(int id,long timestamp,int sequence){
        return capsuleDir+id+"/"+timestamp+"-"+sequence+".mp4";
    }


    static public void startDownload(String url, final String filename) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DownloadRequest downloadRequest = retrofit.create(DownloadRequest.class);

        Call<ResponseBody> responseBodyCall = downloadRequest.downloadPicture(url);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                //建立一个文件
//                File file2= new File(filename);
//                if(file2.length()>0){
//                    return;
//                }
                final File file          = FileUtil.createFile(filename);

                //下载文件放在子线程
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //保存到本地
                        FileUtil.writeFile2Disk(response, file);
                        System.out.println(filename+" finished download");
                        Global.downloadCount++;
                    }
                }.start();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }



    static public void uploadFile(String filepath){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Global.baseUrl)
                .build();
        UploadRequest service = retrofit.create(UploadRequest.class);
        File file = new File(filepath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        System.out.println(filepath+"exist?"+file.exists());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //这里设置的是传过去的文件名
        MultipartBody.Part body = MultipartBody.Part.createFormData("File", file.getName(), requestFile);

        //这边传过去的key value是 description - descriptionString
        String descriptionString = filepath.replace(FileUtil.fileDir,"");
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
