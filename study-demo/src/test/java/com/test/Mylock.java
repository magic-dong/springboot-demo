package com.test;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class Mylock {
	// setup to use Unsafe.compareAndSwapInt for updates
	private static final Unsafe unsafe;
	private static final long valueOffset;

	static {
		try {
			Class<Unsafe> unsafeClazz=Unsafe.class;
			Field theUnsafe = unsafeClazz.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe=(Unsafe) theUnsafe.get(null);
			valueOffset = unsafe.objectFieldOffset(Mylock.class.getDeclaredField("value"));
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	private volatile int value;

	 /**
     * Gets the current value.
     *
     * @return the current value
     */
    public final int get() {
        return value;
    }

    /**
     * Sets to the given value.
     *
     * @param newValue the new value
     */
    public final void set(int newValue) {
        value = newValue;
    }

    
	public static void main(String[] args) {
		System.out.println(unsafe+"============"+valueOffset);
	}
	
	@SuppressWarnings("static-access")
	public void lock(){
		for(;;){
			if(unsafe.compareAndSwapInt(this, valueOffset, 0, 1)){
				return;
			}
			Thread.currentThread().yield();
		}
	}
	
	public void unlock(){
		this.value=0;	
	}
}
