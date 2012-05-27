package br.edu.ufcg.async;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Base64;


public class DecodeJson {

	
	private static <T> List<Field> getFields(Class<T> classz) {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : classz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			fields.add(field);
		}
		return fields;
	}

	private static Map<String, List<String>> extraiDados(List<Field> fields, String json) {
		Map<String, List<String>> mapa = new HashMap<String, List<String>>();
		for (Field field : fields) {
			String regex = "\"" +field.getName() + "\":";
			Pattern p = Pattern.compile(regex);
			json = json.replace("{", "");
			json = json.replace("}", "");
			Matcher m = p.matcher(json);
			while (m.find()) {
				String valor = "";
				try {
					valor = json.substring(m.end(), m.end() + json.substring(m.end()).indexOf(',')).replace("\"", "");
				} catch (IndexOutOfBoundsException e) {
					valor = json.substring(m.end(), m.end() + json.substring(m.end()).indexOf(']')).replace("\"", "");
				}
				if (mapa.containsKey(field.getName())) {
					mapa.get(field.getName()).add(valor);
				} else {
					List<String> valores = new ArrayList<String>();
					valores.add(valor);
					mapa.put(field.getName(), valores);
				}
			}
		}
		return mapa;
	}

	public static <T> List<T> decode(Class<T> classz, String json) {
		List<Field> fields = getFields(classz);
		Map<String, List<String>> mapa = extraiDados(fields, json);
		
		List<T> lista = new ArrayList<T>();
		int tamanhoLista = mapa.get(fields.get(0).getName()).size();
		for (int i = 0; i < tamanhoLista; i++) {
			T objeto = null;

			try {
				objeto = classz.newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (Field f : fields) {
				try {
					f.setAccessible(true);

					if (f.getType().equals(Integer.TYPE)) {
						f.set(objeto, Integer.parseInt(mapa.get(f.getName()).get(i)));
					} else if (f.getType().equals(byte[].class)) {
						byte[] valor = Base64.decode(mapa.get(f.getName()).get(i), Base64.DEFAULT);
						f.set(objeto, valor);
					} else {
						f.set(objeto, mapa.get(f.getName()).get(i));
					}
					f.setAccessible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			lista.add(objeto);
		}

		return lista;
	}

}
