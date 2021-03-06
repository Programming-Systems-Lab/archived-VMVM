package edu.columbia.cs.psl.vmvm.asm.mvs;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;

import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import edu.columbia.cs.psl.vmvm.VirtualRuntime;

public class SystemPropertyLogger extends GeneratorAdapter {

	
	public SystemPropertyLogger(final int api, final MethodVisitor mv,
            final int access, final String name, final String desc) {
		super(api,mv,access,name,desc);
	}
	public static void main(String[] args)
	{
		for(String clazz : VirtualRuntime.internalStatics.keySet())
			{
			VirtualRuntime.InternalStaticClass c = VirtualRuntime.internalStatics.get(clazz);
				for(String s : c.setMethods.keySet())
				{
					String fullSetMethod = clazz.replace("/", ".")+"."+s;
					String castType = "";
					try {
						Class cl = Class.forName(clazz.replace("/", "."));
						for(Method m : cl.getMethods())
						{
							if(m.getName().equals(s) && m.getParameterTypes().length == 1)
							{
								castType = m.getParameterTypes()[0].getCanonicalName();
							}
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("if(logsUsed["+c.setMethods.get(s)+"]){try{");
					System.out.println(fullSetMethod+"(("+castType+")loggedValues["+c.setMethods.get(s)+"]);}catch(Exception ex){ex.printStackTrace();}logsUsed["+c.setMethods.get(s)+"]=false;loggedValues["+c.setMethods.get(s)+"]=null;");
					System.out.println("}");
				}
				
//				for(String s : c.addMethods.keySet())
//				{
//					String fullSetMethod = clazz.replace("/", ".")+"."+s.replace("add", "remove");
//					String castType = "";
//					try {
//						Class cl = Class.forName(clazz.replace("/", "."));
//						for(Method m : cl.getMethods())
//						{
//							if(m.getName().equals(s) && m.getParameterTypes().length == 1)
//							{
//								castType = m.getParameterTypes()[0].getCanonicalName();
//							}
//						}
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println("if(logsUsed["+c.addMethods.get(s)+"]){");
//					System.out.println("for(WeakReference<Object> o : (LinkedList<WeakReference<Object>>) loggedValues["+c.addMethods.get(s)+"])");
//					System.out.println("if(!o.isEnqueued()) "+fullSetMethod+"(("+castType+")o.get());");
//					System.out.println("loggedValues["+c.addMethods.get(s)+"]=null;logsUsed["+c.addMethods.get(s)+"]=false;}");
//				}
//				
//				
//				for(String s : c.removeMethods.keySet())
//				{
//					String fullSetMethod = clazz.replace("/", ".")+"."+s.replace("remove", "add");
//					String castType = "";
//					try {
//						Class cl = Class.forName(clazz.replace("/", "."));
//						for(Method m : cl.getMethods())
//						{
//							if(m.getName().equals(s) && m.getParameterTypes().length == 1)
//							{
//								castType = m.getParameterTypes()[0].getCanonicalName();
//							}
//						}
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println("if(logsUsed["+c.removeMethods.get(s)+"]){");
//					System.out.println("for(Object o : (LinkedList<Object>) loggedValues["+c.removeMethods.get(s)+"])");
//					System.out.println(fullSetMethod+"(("+castType+")o);");
//					System.out.println("loggedValues["+c.removeMethods.get(s)+"]=null;logsUsed["+c.removeMethods.get(s)+"]=false;}");
//				}
			}
	}
	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {

		if((owner.equals("java/lang/System") && name.equals("setProperty") && desc.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
				|| (owner.equals("java/lang/System") && name.equals("setProperties") && desc.equals("(Ljava/util/Properties;)V")))
		{
			owner = Type.getInternalName(VirtualRuntime.class);
			name = "logAndSetProperty";
		}
		else if(VirtualRuntime.internalStatics.containsKey(owner))
		{
			Type[] args =Type.getArgumentTypes(desc);
			VirtualRuntime.InternalStaticClass clazz = VirtualRuntime.internalStatics.get(owner);
			if(clazz.setMethods.keySet().contains(name) && args.length == 1)
			{
				//insert a fake get
				String newName = name.replace("set", "get");
				if(owner.equals("javax/swing/JDialog") || owner.equals("javax/swing/JFrame"))
					newName = name.replace("set", "is");
				super.visitMethodInsn(opcode, owner, newName, Type.getMethodDescriptor(args[0]));
				//Do the log
				super.visitIntInsn(Opcodes.BIPUSH, clazz.setMethods.get(name));
				//box if necessary
				box(args[0]);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(VirtualRuntime.class), "logStaticInternal", "(Ljava/lang/Object;I)V");
			}
			else if(clazz.addMethods.containsKey(name))
			{
				super.visitInsn(Opcodes.DUP);
				//Do the log
				super.visitIntInsn(Opcodes.BIPUSH, clazz.addMethods.get(name));
				//box if necessary
				box(args[0]);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(VirtualRuntime.class), "logStaticInternalAdd", "(Ljava/lang/Object;I)V");
			}
			else if(clazz.removeMethods.containsKey(name))
			{
				super.visitInsn(Opcodes.DUP);
				//Do the log
				super.visitIntInsn(Opcodes.BIPUSH, clazz.addMethods.get(name));
				//box if necessary
				box(args[0]);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(VirtualRuntime.class), "logStaticInternalRemove", "(Ljava/lang/Object;I)V");
			}
		}
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	
}
