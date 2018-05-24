package me.fandong.cloudserver.Controller;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.fandong.cloudserver.Model.WingManProductListModel;
import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.Model.WingManUserModel;
import me.fandong.cloudserver.dao.impl.WingManImpl;
import me.fandong.cloudserver.service.WingManService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class WingManController {
    private StringMap stringMap;

    @Autowired
    WingManService wingManService;
    private static Logger logger = Logger.getLogger(WingManController.class);

    @ApiOperation(value="获取WingMan应用内产品列表", notes="查询WingMan应用内产品列表")
    @GetMapping("/getWingManProductList")
    public String getWingManProductList(){
        stringMap = new StringMap();
        List<WingManProductModel> list = wingManService.listWingManProducts();
        WingManProductListModel model = new WingManProductListModel(list);
        stringMap.put("data",model);
        stringMap.put("status",1);
        stringMap.put("msg","获取WingMan应用内产品列表数据成功");
        logger.info("result:"+Json.encode(stringMap));
        return Json.encode(stringMap);
    }

    @ApiOperation(value="获取WingMan应用内用户", notes="查询WingMan应用内用户")
    @ApiImplicitParam(name = "uuid", value = "uuid", required = true, dataType = "string")
    @GetMapping("/getWingManUser")
    public String getWingManUser(String uuid){
        stringMap = new StringMap();
        if (uuid == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","uuid为空");
            return Json.encode(stringMap);
        }
        ArrayList<WingManUserModel> list = (ArrayList<WingManUserModel>)wingManService.getWingManUserModel(uuid);
        if (!list.isEmpty()) {
            stringMap.put("data", list);
            stringMap.put("status", 1);
            stringMap.put("msg", "获取WingMan应用内产品用户数据成功");
            logger.info("result:" + Json.encode(stringMap));
            return Json.encode(stringMap);
        }else {
            wingManService.createWingManUserModel(uuid);
            ArrayList<WingManUserModel> tempList = (ArrayList<WingManUserModel>)wingManService.getWingManUserModel(uuid);
            if (!tempList.isEmpty()){
                stringMap.put("data", list);
                stringMap.put("status", 1);
                stringMap.put("msg", "获取WingMan应用内产品用户数据成功");
                logger.info("result:" + Json.encode(stringMap));
                return Json.encode(stringMap);
            }else {
                return getWingManUser(uuid);
            }
        }
    }

    @ApiOperation(value="更新WingMan用户过期时间", notes="更新WingMan用户过期时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "uuid", required = true, dataType = "string"),
            @ApiImplicitParam(name = "productId", value = "productId", required = true, dataType = "string")
    })
    @PostMapping("/updateWingManUser")
    public String updateWingManUser(String uuid, String productId){
        stringMap = new StringMap();
        if (uuid == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","uuid为空");
            return Json.encode(stringMap);
        }
        if (productId == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","productId为0");
            return Json.encode(stringMap);
        }

        int day = 0;
        if (productId.equals("me.fandong.WingMan.Product01")){
            day = 7;
        }else if (productId.equals("me.fandong.WingMan.Product02")){
            day = 30;
        }else if (productId.equals("me.fandong.WingMan.Product03")){
            day = 90;
        }else if (productId.equals("me.fandong.WingMan.Product04")){
            day = 365;
        }else if (productId.equals("me.fandong.WingMan.Product05")){
            day = 1095;
        }

        ArrayList<WingManUserModel> list = (ArrayList<WingManUserModel>)wingManService.getWingManUserModel(uuid);
        WingManUserModel model = list.get(0);
        String expireDateString = model.getExpireDate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date expireDate = formatter.parse(expireDateString);
            Date currentDate = new Date();
            if (expireDate.before(currentDate)){
                //如果过期时间早于当前时间，在当前时间基础上加day
                Date newExpireDate = WingManImpl.getDateAfter(currentDate,day);
                String newExpireDateString = formatter.format(newExpireDate);
                wingManService.updateWingManUserModel(uuid,newExpireDateString);
                stringMap.put("data","");
                stringMap.put("status",1);
                stringMap.put("msg","更新过期时间成功");
                logger.info("result:" + Json.encode(stringMap));
                return Json.encode(stringMap);
            }else {
                //如果过期时间晚于当前时间，在当前过期时间基础上加day
                Date newExpireDate = WingManImpl.getDateAfter(expireDate,day);
                String newExpireDateString = formatter.format(newExpireDate);
                wingManService.updateWingManUserModel(uuid,newExpireDateString);
                stringMap.put("data","");
                stringMap.put("status",1);
                stringMap.put("msg","更新过期时间成功");
                logger.info("result:" + Json.encode(stringMap));
                return Json.encode(stringMap);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg",e.getMessage());
            logger.info("result:" + Json.encode(stringMap));
            return Json.encode(stringMap);
        }
    }
}
