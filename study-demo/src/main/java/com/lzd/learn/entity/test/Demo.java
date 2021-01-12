package com.lzd.learn.entity.test;

public class Demo{
	public Demo(Test d){
		d.sayHello();
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Demo t=new Demo(new Test() {
			@Override
			public void sayHello() {
				// TODO Auto-generated method stub
				System.out.println("我是匿名内部类：");
				System.out.println("Class对象是："+this.getClass());
				System.out.println("类名字是："+this.getClass().getSimpleName());
			}
		});
		
		A a=new A() {
			@Override
			void hello() {
				// TODO Auto-generated method stub
				System.out.println("Class对象是："+this.getClass());
				System.out.println("类名字是："+this.getClass().getSimpleName());
			}
		};
		a.hello();
	}
}

interface Test {
	void sayHello();
}

abstract class A{
	abstract void hello();
	
	void aa(){
		System.out.println("A.aa()");
	}
}