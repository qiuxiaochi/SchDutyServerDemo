package verticleServer.handlers.userhandler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import threadUtil.FixedThreadPool;
import util.EncryptTool;
import util.MyTools;
import verticleServer.entity.user.RegPhone;
import verticleServer.httpUtil.CacheService;
import verticleServer.httpUtil.HttpUtil;


/*
 * 	进入手机号注册界面   获得图片验证码
 */
public class RegImageCodeHandler implements Handler<RoutingContext>{

	
	public RegImageCodeHandler() {
		
	}
	
	@Override
	public void handle(RoutingContext ctx) {
		
		JsonObject bodyAsJson = ctx.getBodyAsJson();
		
		//System.out.println("注册手机号   图片验证码请求");
		FixedThreadPool.threadPool.submit(new Runnable() {
			
			@Override
			public void run() {
				String phone = bodyAsJson.getString("phone");
				
				String imageCode = MyTools.getRandomPassword(4);
				
				String encrypt =(MyTools.getRandomPassword(10));
				
//				System.out.println(imageCode);
				
				if(!CacheService.phoneRegMap.containsKey(phone)) {
					RegPhone regPhone = new RegPhone();
					regPhone.setPhone(phone);
					CacheService.phoneRegMap.put(phone, regPhone);
				}
				CacheService.phoneRegMap.get(phone).setImageCode(imageCode);
				CacheService.phoneRegMap.get(phone).setLasttime(System.currentTimeMillis());
				CacheService.phoneRegMap.get(phone).setEncrypt(encrypt);
				
//				System.out.println(encrypt+"   5555");
				encrypt = EncryptTool.encryptStr(encrypt);
//				System.out.println(encrypt+"   5555");
				JsonObject jsonObject = new JsonObject();
				jsonObject.put("imageCode", imageCode);
				jsonObject.put("encrypt", encrypt);
				HttpUtil.resp(ctx.request(), jsonObject);
				
				
			}
		});
		
		
	}
	
}
