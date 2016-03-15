package com.winning.pregnancy.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json数据 工具类
 * 
 * @author zhangshuaipeng
 */
public class JsonBuilder {
	/**
	 * 得到JsonBuilder实例化对象
	 * 
	 * @return
	 */
	public static JsonBuilder getInstance() {
		return JsonHolder.JSON_BUILDER;
	}

	/**
	 * 静态内部类
	 * 
	 * @author zhangshuaipeng
	 */
	private static class JsonHolder {
		private static final JsonBuilder JSON_BUILDER = new JsonBuilder();
		private static ObjectMapper mapper = new ObjectMapper();
	}

	/**
	 * 将一个数据实体解析成Json数据格式
	 * 
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj) {
		try {
			return JsonHolder.mapper.writeValueAsString(obj);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将一个Json字符串封装为指定类型对象
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public Object fromJson(String json, Class<?> c) {
		json = cleanJson(json);
		try {
			Object obj = JsonHolder.mapper.readValue(json, c);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将一个JsonArray数据转换成一个List的键值对 [{name:'zsp',value:1},{name:'zsp',value:2}]
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> fromJsonArray(String json) {
		json = cleanJson(json);
		List<Map> dataList = (List<Map>) fromJson(json, ArrayList.class);

		return dataList;
	}

	/**
	 * 为操作成功返回Json
	 * 
	 * @param strData
	 * @return
	 */
	public String returnSuccessJson(String strData) {
		StringBuffer returnJson = new StringBuffer("{ success : true, obj : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}

	/**
	 * 为操作失败返回Json
	 * 
	 * @param strData
	 * @return
	 */
	public String returnFailureJson(String strData) {
		StringBuffer returnJson = new StringBuffer("{ success : false, obj : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}

	/**
	 * 为分页列表提供Json封装
	 * 
	 * @param count
	 *            记录总数
	 * @param entities
	 *            实体列表
	 * @param excludes
	 *            在json生成时需要排除的属性名称
	 * @return
	 */
	public String buildObjListToJson(Long count,
			Collection<? extends Object> records, boolean listJson) {
		try {
			StringBuffer pageJson = null;
			// 判断是否展示list的数据
			if (listJson) {
				pageJson = new StringBuffer("{totalCount:" + count + ","
						+ "rows" + ":");
			} else {
				pageJson = new StringBuffer("");
			}
			// 构建集合的json数据
			StringWriter w = new StringWriter();
			JsonHolder.mapper.writeValue(w, records);
			pageJson.append(w);
			w.close();

			if (listJson) {
				pageJson.append("}");
			} else {
				pageJson.append("");
			}
			return pageJson.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化Json
	 * 
	 * @param json
	 * @return
	 */
	public String cleanJson(String json) {
		if (StringUtil.isNotEmpty(json)) {
			return json.replaceAll("\n", "");
		} else {
			return "";
		}
	}

	/**
	 * 将json 数组转换为Map 对象
	 * 
	 * @param jsonString
	 * @return
	 */
	public Map<String, Object> getMap(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = (String) keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key.toLowerCase(), value);
			}
			return valueMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把json 转换为 ArrayList 形式
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getList(String jsonString) {
		List<Map<String, Object>> list = null;
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(getMap(jsonObject.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Object fromMapJson(Map<String, Object> map, Class<?> c) {
		try {
			String json = toJson(map);
			json = cleanJson(json);
			Object obj = JsonHolder.mapper.readValue(json, c);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object fromMapStrJson(Map<String, String> map, Class<?> c) {
		try {
			String json = toJson(map);
			json = cleanJson(json);
			Object obj = JsonHolder.mapper.readValue(json, c);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
