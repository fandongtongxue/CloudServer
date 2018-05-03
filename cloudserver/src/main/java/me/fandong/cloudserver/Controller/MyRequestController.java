package me.fandong.cloudserver.Controller;

import me.fandong.cloudserver.Model.MyRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/myrequest")
public class MyRequestController {
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public MyRequest getMyRequest(@PathVariable int id) {
        MyRequest request = new MyRequest();
        request.setId(id);
        request.setParam(""+id);
        request.setUrl("/myrequest/"+id);
        request.setMsg("测试");
        request.setStatus("1");
        return request;
    }
}
