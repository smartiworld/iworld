package iworld.rpc.utils.serializer;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.smart.iworld.rpc.api.SmartSerializer;

public class JsonSerializer implements SmartSerializer {

	private Charset charset;
	
	public JsonSerializer(String charset) {
		if(charset == null || Strings.isNullOrEmpty(charset)) {
			throw new NullPointerException("charset is null");
		}
		this.charset = Charset.forName(charset);
	}
	
	public JsonSerializer(Charset charset) {
		if(charset == null) {
			throw new NullPointerException("charset is null");
		}
		this.charset = charset;
	}
	
	@Override
	public byte[] serializer(Object data) {
		return JSON.toJSONString(data).getBytes(charset);
	}

	@Override
	public <T> T deSerializer(byte[] datas, Class<T> resultType) {
		String jsonStr = new String(datas, charset);
		return JSON.parseObject(jsonStr, resultType);
	}

}
