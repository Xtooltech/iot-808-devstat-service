package com.xtool.devstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.xtool.enterprise.RespState;
import com.xtool.enterprise.data.DataSearchResult;
import com.xtool.iot808data.devstat.EnableDevStatMaintainer;
import com.xtool.iot808data.devstat.devstatCondition;
import com.xtool.iot808data.devstat.devstatMaintainer;
import com.xtool.iot808data.devstat.devstatModel;

@RestController
@EnableDevStatMaintainer
public class DevStatController {

	@Autowired
	devstatMaintainer dataMaintainer;
	
	@RequestMapping(path="/devstat/upsert",method= {RequestMethod.POST},headers= {"Content-Type=application/json"})
	public RespState<Boolean> upsert(@RequestBody devstatModel data){
		RespState<Boolean> result=new RespState<>();
		if(data==null || StringUtils.isEmpty(data.sno)) {
			result.setCode(406);
			result.setMsg("Not acceptable");
			result.setData(false);
		}else {
			try {
				boolean opt=dataMaintainer.upsert(data,true);
				result.setCode(opt?0:501);
				result.setMsg(opt?"ok":"failed");
				result.setData(opt);
			}catch (Exception e) {
				result.setCode(500);
				result.setMsg(e.getMessage());
				result.setData(false);
			}
		}
		return result;
	}
	
	@RequestMapping(value="/devstat/get",method= {RequestMethod.POST})
	public RespState<DataSearchResult<devstatModel>> get(
			@RequestBody 
			devstatCondition condition){
		
		if(dataMaintainer==null)return null;
		if(condition==null) {
			condition=new devstatCondition();
			condition.setPageIndex(1);
			condition.setPageSize(1);
		}
		RespState<DataSearchResult<devstatModel>> result=new RespState<DataSearchResult<devstatModel>>();
		try {
			DataSearchResult<devstatModel> data= dataMaintainer.search(condition);
			result.setData(data);
			result.setCode(0);
			result.setMsg("");
		}catch(Exception ex) {
			result.setData(null);
			result.setCode(500);
			result.setMsg(ex.getMessage());
		}
		return result;
	}
}
