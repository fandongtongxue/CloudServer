package me.fandong.cloudserver.Controller;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import me.fandong.cloudserver.Model.AliyunOSSBucketListModel;
import me.fandong.cloudserver.Model.AliyunOSSBucketModel;
import me.fandong.cloudserver.Model.FileModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ListAliyunOSSBucketController {
    private StringMap stringMap;

    @RequestMapping("/getAliyunOSSBucketList")
    public String getAliyunOSSBucketList(HttpServletRequest request) {
        stringMap = new StringMap();

        String accessKeyId = request.getParameter("accessKeyId");
        String accessKeySecret = request.getParameter("accessKeySecret");
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
        System.out.println(request.toString());
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 列举bucket
        List<Bucket> buckets = ossClient.listBuckets();

        AliyunOSSBucketListModel model = new AliyunOSSBucketListModel();
        // 关闭client
        ossClient.shutdown();

        ArrayList array = new ArrayList<FileModel>();
        for (Bucket bucket:buckets) {
            AliyunOSSBucketModel bucketModel = new AliyunOSSBucketModel();
            bucketModel.setName(bucket.getName());
            bucketModel.setEndPoint(bucket.getExtranetEndpoint());
            array.add(bucketModel);
        }
        model.setList(array);
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","获取Bucket列表数据成功");
        System.out.println(stringMap.toString());
        return Json.encode(stringMap);
    }
}
