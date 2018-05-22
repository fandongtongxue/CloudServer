package me.fandong.cloudserver.Controller;

import com.qiniu.util.Json;
import com.qiniu.util.StringMap;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.fandong.cloudserver.Model.WingManProductListModel;
import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.Model.WingManUserModel;
import me.fandong.cloudserver.dao.impl.WingManImpl;
import me.fandong.cloudserver.service.WingManService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

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
    @GetMapping("/updateWingManUserExpireDate")
    public String updateWingManUserExpireDate(String uuid, int day){
        stringMap = new StringMap();
        if (uuid == null){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","uuid为空");
            return Json.encode(stringMap);
        }
        if (day == 0){
            stringMap.put("data","");
            stringMap.put("status",0);
            stringMap.put("msg","day为0");
            return Json.encode(stringMap);
        }
        ArrayList<WingManUserModel> list = (ArrayList<WingManUserModel>)wingManService.getWingManUserModel(uuid);
        WingManUserModel model = list.get(0);
        String expireDateString = model.getExpireDate();
        System.out.println(expireDateString);
//        WingManImpl.getDateAfter(,day);
        stringMap.put("data","");
        stringMap.put("status",1);
        stringMap.put("msg","更新过期时间成功");
        return Json.encode(stringMap);
    }
}
