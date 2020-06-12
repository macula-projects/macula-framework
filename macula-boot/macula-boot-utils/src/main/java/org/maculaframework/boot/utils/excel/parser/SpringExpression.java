/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * SpringExpression.java 2015年10月20日
 */
package org.maculaframework.boot.utils.excel.parser;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <b>SpringExpression</b> 基于Spring EL的表达式解析
 * </p>
 *
 * @since 2015年10月20日
 * @author Rain
 * @version $Id: SpringExpression.java 5907 2015-10-20 07:54:29Z wzp $
 */
public class SpringExpression {

	/**
	 * 评估表达式的值
	 * 
	 * @param expr 表达式
	 * @param context 上下文数据
	 * @param requiredType 返回的类型
	 * @return 表达式的值
	 */
	public static <T> T eval(Object context, String expr, Class<T> requiredType) {
		EvaluationContext evalContext = new StandardEvaluationContext(context);
		evalContext.getPropertyAccessors().add(new MapAccessor());
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(expr);
		return expression.getValue(evalContext, requiredType);
	}

	public static void main(String[] args) {
		Map<String, Object> context = new HashMap<String, Object>();
		User user = new User("rain", "password");
		context.put("user", user);
		context.put("userName", "hello world!");
		
		System.out.println(SpringExpression.eval(context, "userName", String.class));
		System.out.println(SpringExpression.eval(context, "user.password", String.class));
	}

	static class User {
		private String userName;
		private String password;
		
		public User(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
