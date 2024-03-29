package me.fandong.cloudserver.Controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fandong.cloudserver.Config.ServerConfig;
import me.fandong.cloudserver.Model.FileListModel;
import me.fandong.cloudserver.Model.FileModel;
import me.fandong.cloudserver.service.MyRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class QiniuFileController {
    private StringMap stringMap;
    private static Logger logger = Logger.getLogger(QiniuFileController.class);
    @Autowired
    MyRequestService myRequestService;
    private String filePrefix;

    @ApiOperation(value="获取七牛云存储Bucket的文件列表", notes="根据AK、SK、Bucket、filePrefix、marker查询Bucket的文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "filePrefix", value = "filePrefix", required = false, dataType = "string"),
            @ApiImplicitParam(name = "marker", value = "marker", required = false, dataType = "string")
    })
    @GetMapping("/getQiniuFileList")
    public String getFileList(String bucket, String filePrefix, String marker){
        stringMap = new StringMap();
        String AK = new ServerConfig().AK;
        String SK = new ServerConfig().SK;
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
        logger.info("url:"+"/getQiniuFileList");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket:"+bucket);
        String params ="AK:"+AK+"&SK:"+SK+"&bucket:"+bucket;
        Auth auth = Auth.create(AK, SK);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(bucket, filePrefix, marker, 20, null);
            FileInfo[] items = fileListing.items;
            ArrayList array = new ArrayList<FileModel>();

            for (FileInfo fileInfo : items) {
                FileModel model = new FileModel();
                model.setFileName(fileInfo.key);
                model.setFileSize(fileInfo.fsize/1024 + "KB");
                model.setFileType(fileInfo.mimeType);
                array.add(model);
            }
            FileListModel listModel = new FileListModel();
            listModel.setList(array);
            listModel.setMarker(fileListing.marker);
            if (fileListing.marker == null){
                listModel.setMarker("");
            }
            stringMap.put("data",listModel);
            stringMap.put("status",1);
            stringMap.put("msg","获取文件列表数据成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/getQiniuFileList",params,Json.encode(stringMap),"0",e.error());
            return Json.encode(stringMap);
        }
    }

    @ApiOperation(value="删除七牛云存储Bucket的文件", notes="根据AK、SK、Bucket、fileName删除Bucket的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "bucket", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "fileName", required = true, dataType = "string"),
    })
    @PostMapping("/deleteQiniuFile")
    public String deleteFile(String bucket, String fileName){
        stringMap = new StringMap();
        String AK = new ServerConfig().AK;
        String SK = new ServerConfig().SK;
        String key = fileName;
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
        logger.info("url:"+"/deleteQiniuFile");
        logger.info("params:"+"AK:"+AK+" SK:"+SK+" bucket:"+bucket+" key:"+key);
        String params ="AK:"+AK+"&SK:"+SK+"&bucket:"+bucket+"&key:"+key;
        Auth auth = Auth.create(AK, SK);
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            bucketManager.delete(bucket,key);
            stringMap.put("data","");
            stringMap.put("status",1);
            stringMap.put("msg","删除文件成功");
            logger.info("result:"+Json.encode(stringMap));
            return Json.encode(stringMap);
        } catch (QiniuException e) {
            Response r = e.response;
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.error());
            logger.info("result:"+Json.encode(stringMap));
            myRequestService.createMyRequest("/deleteQiniuFile",params,Json.encode(stringMap),"0",e.error());
            return Json.encode(stringMap);
        }
    }
}
