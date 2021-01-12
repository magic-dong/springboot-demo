package com.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Test2 {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		//获取到单例对象
        SingletonSerializable instance = SingletonSerializable.getInstance();
        
        //序列化
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\temp");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instance);
        
        //反序列化
        FileInputStream fileInputStream = new FileInputStream("F:\\temp");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        SingletonSerializable instance2 = (SingletonSerializable)objectInputStream.readObject();
        
        System.out.println("instance:"+instance);
        System.out.println("instance2:"+instance2);
        System.out.println(instance==instance2);
	}

}

/**
 * 序列化的单例类
 * @author lzd
 * @date 2019年6月5日
 * @version
 */
class SingletonSerializable implements Serializable{
	
	private static final long serialVersionUID = -1131868478960947804L;
	
	//静态属性
	private static volatile SingletonSerializable singleton=null;
	
	//私有构造
	private SingletonSerializable(){
		
	}
	
	/**
	 * Double-checked
	 * @author lzd
	 * @date 2019年6月5日:下午5:05:21
	 * @return
	 * @description
	 */
	public static final SingletonSerializable  getInstance(){
		if(singleton==null){
			synchronized(SingletonSerializable.class){
				if(singleton==null){
					singleton=new SingletonSerializable();
				}
			}
		}
		return singleton;
	}
	
	/**
	 * 单例类中定义此方法
	 * 就可以解决序列化反射破解单例的问题
	 * @author lzd
	 * @date 2019年6月5日:下午5:56:07
	 * @return
	 * @description
	 */
	private Object readResolve(){
		return singleton;
	}
}
