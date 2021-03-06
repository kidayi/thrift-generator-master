package com.sohu.thrift.generator.builder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.sohu.thrift.generator.Constants;
import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftField;
import com.sohu.thrift.generator.ThriftStruct;

public class ThriftFieldBuilder {
	
	public ThriftField buildThriftField(ThriftStructBuilder structBuilder, Field field, List<ThriftStruct> structs,Map<String,Object> mapStructs, List<ThriftEnum> enums) {
		ThriftField thriftField = new ThriftField();
		thriftField.setName(field.getName());
		if(field.getName().equals("__PARANAMER_DATA")) {
			return null;
		}
		Type type = field.getGenericType();
		Generic generic = Generic.fromType(field.getGenericType());
		thriftField.setGenericType(generic);
		if(type instanceof ParameterizedType) {
			structBuilder.buildStrutsByGeneric(structs,mapStructs, generic, enums);
		}else {
			if(generic.isEnum() || generic.isStruct()) {
				generic.setJavaClass(field.getType());
				generic.setValue(field.getType().getSimpleName());
				generic.setJavaTypeName(field.getType().getSimpleName());
				if(generic.isStruct()) {
					generic.setValue(field.getType().getSimpleName()+Constants.end_str);
					generic.setJavaTypeName(field.getType().getSimpleName()+Constants.end_str);
					structBuilder.buildThriftStruct(field.getType(), structs,mapStructs, enums);
				}else {
					enums.add(structBuilder.buildThriftEnum(field.getType()));
				}
			}
		}
		return thriftField;
	}
}
