package me.fandong.cloudserver.Controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.fandong.cloudserver.Model.FilePathModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.springframework.web.bind.annotation.RequestMethod;
import com.qiniu.storage.UploadManager;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ClientException;

@RestController
public class UploadController {
    private StringMap stringMap;

    private static Logger logger = Logger.getLogger(UploadController.class);

    @Autowired
    MyRequestService myRequestService;
    @PostMapping("/uploadImageFile")
    public String uploadImageFile(MultipartHttpServletRequest multiRequest) throws IOException{
        stringMap = new StringMap();

        //把当前时间戳作为字符串
        String key = System.currentTimeMillis() + ".jpg";
        //测试服务器文件路径13
//        File file = new File("/Users/fandong/Git/CloudServer/cloudserver/src/main/resources/static/"+key);
        //测试服务器文件路径15
//        File file = new File("/Users/fandongtongxue/Documents/GitHub/CloudServer/cloudserver/src/main/resources/static/"+key);
        //正式服务器文件路径(要可直接访问,40GB高效云盘地址以供云盘上传使用)
        //美国服务器
        File file = new File("/home/static/"+key);
        //如果文件路径不存在,创建父目录,创建当前文件路径
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输入输出流
        FileOutputStream fos = new FileOutputStream(file);
        FileInputStream fis = (FileInputStream) multiRequest.getFile("imageFile").getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }
        fos.close();
        fis.close();

        //上传之后的路径放到Data
        FilePathModel model = new FilePathModel();
        model.setFilePath(file.getPath());
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","文件上传成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @PostMapping("/uploadVideoFile")
    public String uploadVideoFile(MultipartHttpServletRequest multiRequest) throws IOException{
        stringMap = new StringMap();

        //把当前时间戳作为字符串
        String key = System.currentTimeMillis() + ".mov";
        //测试服务器文件路径13
//        File file = new File("/Users/fandong/Git/CloudServer/cloudserver/src/main/resources/static/"+key);
        //测试服务器文件路径15
//        File file = new File("/Users/fandongtongxue/Documents/GitHub/CloudServer/cloudserver/src/main/resources/static/"+key);
        //正式服务器文件路径(要可直接访问,40GB高效云盘地址以供云盘上传使用)
        //美国服务器
        File file = new File("/home/static/"+key);
        //如果文件路径不存在,创建父目录,创建当前文件路径
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输入输出流
        FileOutputStream fos = new FileOutputStream(file);
        FileInputStream fis = (FileInputStream) multiRequest.getFile("videoFile").getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }
        fos.close();
        fis.close();

        //上传之后的路径放到Data
        FilePathModel model = new FilePathModel();
        model.setFilePath(file.getPath());
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","文件上传成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @ApiOperation(value="上传文件到七牛云存储", notes="根据AK、SK、Bucket、key、filePath上传文件到七牛云")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AK", value = "AK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "SK", value = "SK", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "key", required = true, dataType = "string"),
            @ApiImplicitParam(name = "filePath", value = "filePath", required = true, dataType = "string")
    })
    @RequestMapping(value="/uploadQiniuFile",method=RequestMethod.POST)
    public String uploadQiniuFile(String AK, String SK, String bucket, String key, String filePath) {
        stringMap = new StringMap();
        if (AK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","AK为空");
            return Json.encode(stringMap);
        }
        if (SK == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","SK为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        if (key == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","key为空");
            return Json.encode(stringMap);
        }
        if (filePath == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","filePath为空");
            return Json.encode(stringMap);
        }
        logger.info("url:"+"/uploadQiniuFile");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket:"+bucket);
        String params ="AK:"+AK+"&SK:"+SK+"&bucket:"+bucket;
        Auth auth = Auth.create(AK, SK);

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        UploadManager uploadManager = new UploadManager(c);
        try {
            uploadManager.put(filePath,key,auth.uploadToken(bucket));
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件上传到七牛云成功");
            logger.info("result:"+Json.encode(stringMap));
            File file = new File(filePath);
            file.delete();
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            Response r = e.response;
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/uploadQiniuFile",params,Json.encode(stringMap),"0",e.error());
            return Json.encode(stringMap);
        }
    }

    @ApiOperation(value="上传文件到阿里云OSS", notes="根据accessKeyId、accessKeySecret、bucket、endPoint、key、filePath上传文件到阿里云OSS")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessKeyId", value = "accessKeyId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "accessKeySecret", value = "accessKeySecret", required = true, dataType = "string"),
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "endPoint", value = "endPoint", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "key", required = true, dataType = "string"),
            @ApiImplicitParam(name = "filePath", value = "filePath", required = true, dataType = "string")
    })
    @PostMapping("/uploadAliyunFile")
    public String uploadAliyunFile(String accessKeyId, String accessKeySecret, String bucket, String endPoint, String key, String filePath) {
        stringMap = new StringMap();

        //传空值处理
        if (accessKeyId == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeyId为空");
            return Json.encode(stringMap);
        }
        if (accessKeySecret == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","accessKeySecret为空");
            return Json.encode(stringMap);
        }
        if (bucket == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","bucket为空");
            return Json.encode(stringMap);
        }
        if (key == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","key为空");
            return Json.encode(stringMap);
        }
        if (endPoint == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","endPoint为空");
            return Json.encode(stringMap);
        }
        if (filePath == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","filePath为空");
            return Json.encode(stringMap);
        }

        logger.info("url:"+"/uploadAliyunFile");
        logger.info("params:"+"accessKeyId:"+accessKeyId+" accessKeySecret:"+accessKeySecret+" endPoint:"+endPoint+" bucket:"+bucket);
        String params = "accessKeyId:"+accessKeyId+"&accessKeySecret:"+accessKeySecret+"&endPoint:"+endPoint+"&bucket:"+bucket+"&key:"+key;
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);

        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucket, key);
            // The local file to upload---it must exist.
            uploadFileRequest.setUploadFile(filePath);
            // Sets the concurrent upload task number to 5.
            uploadFileRequest.setTaskNum(5);
            // Sets the part size to 1MB.
            uploadFileRequest.setPartSize(1024 * 1024 * 1);
            // Enables the checkpoint file. By default it's off.
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);

            CompleteMultipartUploadResult multipartUploadResult =
                    uploadResult.getMultipartUploadResult();
            System.out.println(multipartUploadResult.getETag());
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","文件上传到阿里云成功");
            logger.info("result:"+Json.encode(stringMap));
            File file = new File(filePath);
            file.delete();
            return Json.encode(stringMap);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/uploadAliyunFile",params,Json.encode(stringMap),"0",oe.getMessage());
            return Json.encode(stringMap);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/uploadAliyunFile",params,Json.encode(stringMap),"0",ce.getErrorMessage());
            return Json.encode(stringMap);
        } catch (Throwable e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","文件上传到阿里云失败");
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/uploadAliyunFile",params,Json.encode(stringMap),"0",e.getMessage());
            return Json.encode(stringMap);
        } finally {
            ossClient.shutdown();
        }
    }
}
