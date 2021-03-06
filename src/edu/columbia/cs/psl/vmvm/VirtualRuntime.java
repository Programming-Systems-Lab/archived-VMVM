package edu.columbia.cs.psl.vmvm;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.activation.ActivationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.sql.rowset.spi.SyncFactoryException;
import javax.swing.UnsupportedLookAndFeelException;

@VMVMInstrumented
public class VirtualRuntime {
	private static Integer nextVM = 1;

	private static HashMap<Long, VMState> threadVMStatus = new HashMap<>();
	public static HashMap<String, String> properties = new HashMap<>();

	private static HashSet<WeakReference<Class<?>>> classes = new HashSet<>();
	private static Queue<WeakReference<Class<?>>> classesInOrder = new LinkedList<>();

	public static Object[] loggedValues = new Object[74];
	public static boolean[] logsUsed = new boolean[74];

	public static void registerClInit(Class<?> c) {
		if (c.getName().startsWith("org.eclipse.jdt.internal.compilerzz.lookup.Binding"))
			return; //TODO do a divide and conquer on all of the classes to see which is causing the problem 
		synchronized (classes) {
			if (!classes.contains(c)) {
				WeakReference<Class<?>> cr = new WeakReference<Class<?>>(c);
				classesInOrder.add(cr);
				classes.add(cr);
			}
		}
	}

	public static HashMap<String, InternalStaticClass> internalStatics = new HashMap<>();
	static {
		int n = 0;
		Scanner s = new Scanner(VirtualRuntime.class.getResourceAsStream("internal-statics"));
		while (s.hasNextLine()) {
			String l = s.nextLine();
			String[] d = l.split("\t");
			String name = d[0];
			InternalStaticClass c = new InternalStaticClass();
			for (String z : d[1].split(",")) {
				if (z.length() > 0)
					c.addMethods.put(z, ++n);
			}
			for (String z : d[2].split(","))
				if (z.length() > 0)
					c.removeMethods.put(z, ++n);
			for (String z : d[3].split(","))
				if (z.length() > 0)
					c.setMethods.put(z, ++n);
			for (String z : d[4].split(","))
				if (z.length() > 0)
					c.getMethods.put(z, -1);
			internalStatics.put(name, c);
		}
	}

	private static void resetInternalStatics() {
		if (logsUsed[44]) {
			try {
				javax.security.auth.Policy.setPolicy((javax.security.auth.Policy) loggedValues[44]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[44] = false;
			loggedValues[44] = null;
		}
		if (logsUsed[13]) {
			try {
				java.lang.Thread.setDefaultUncaughtExceptionHandler((java.lang.Thread.UncaughtExceptionHandler) loggedValues[13]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[13] = false;
			loggedValues[13] = null;
		}
		if (logsUsed[17]) {
			try {
				java.net.URLConnection.setContentHandlerFactory((java.net.ContentHandlerFactory) loggedValues[17]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[17] = false;
			loggedValues[17] = null;
		}
		if (logsUsed[18]) {
			try {
				java.net.URLConnection.setFileNameMap((java.net.FileNameMap) loggedValues[18]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[18] = false;
			loggedValues[18] = null;
		}
		if (logsUsed[19]) {
			try {
				java.net.URLConnection.setDefaultAllowUserInteraction((boolean) loggedValues[19]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[19] = false;
			loggedValues[19] = null;
		}
		if (logsUsed[20]) {//try{
			//java.net.URLConnection.setDefaultRequestProperty(()loggedValues[20]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[20] = false;
			loggedValues[20] = null;
		}
		if (logsUsed[16]) {
			try {
				java.net.ResponseCache.setDefault((java.net.ResponseCache) loggedValues[16]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[16] = false;
			loggedValues[16] = null;
		}
		if (logsUsed[37]) {
			try {
				javax.imageio.ImageIO.setUseCache((boolean) loggedValues[37]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[37] = false;
			loggedValues[37] = null;
		}
		if (logsUsed[38]) {
			try {
				javax.imageio.ImageIO.setCacheDirectory((java.io.File) loggedValues[38]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[38] = false;
			loggedValues[38] = null;
		}
		if (logsUsed[35]) {
			try {
				javax.activation.CommandMap.setDefaultCommandMap((javax.activation.CommandMap) loggedValues[35]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[35] = false;
			loggedValues[35] = null;
		}
		if (logsUsed[63]) {
			try {
				javax.swing.plaf.synth.SynthLookAndFeel.setStyleFactory((javax.swing.plaf.synth.SynthStyleFactory) loggedValues[63]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[63] = false;
			loggedValues[63] = null;
		}
		if (logsUsed[55]) {
			try {
				javax.swing.Timer.setLogTimers((boolean) loggedValues[55]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[55] = false;
			loggedValues[55] = null;
		}
		if (logsUsed[43]) {
			try {
				javax.net.ssl.SSLContext.setDefault((javax.net.ssl.SSLContext) loggedValues[43]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[43] = false;
			loggedValues[43] = null;
		}
		if (logsUsed[21]) {
			try {
				java.rmi.activation.ActivationGroup.setSystem((java.rmi.activation.ActivationSystem) loggedValues[21]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[21] = false;
			loggedValues[21] = null;
		}
		if (logsUsed[41]) {
			try {
				javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory((javax.net.ssl.SSLSocketFactory) loggedValues[41]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[41] = false;
			loggedValues[41] = null;
		}
		if (logsUsed[42]) {
			try {
				javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((javax.net.ssl.HostnameVerifier) loggedValues[42]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[42] = false;
			loggedValues[42] = null;
		}
		if (logsUsed[54]) {
			try {
				javax.swing.PopupFactory.setSharedInstance((javax.swing.PopupFactory) loggedValues[54]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[54] = false;
			loggedValues[54] = null;
		}
		if (logsUsed[51]) {
			try {
				javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled((boolean) loggedValues[51]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[51] = false;
			loggedValues[51] = null;
		}
		if (logsUsed[14]) {
			try {
				java.net.CookieHandler.setDefault((java.net.CookieHandler) loggedValues[14]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[14] = false;
			loggedValues[14] = null;
		}
		if (logsUsed[46]) {
			try {
				javax.sql.rowset.spi.SyncFactory.setLogger((java.util.logging.Logger) loggedValues[46]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[46] = false;
			loggedValues[46] = null;
		}
		if (logsUsed[47]) {
			try {
				javax.sql.rowset.spi.SyncFactory.setJNDIContext((javax.naming.Context) loggedValues[47]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[47] = false;
			loggedValues[47] = null;
		}
		if (logsUsed[67]) {
			try {
				java.net.Authenticator.setDefault((java.net.Authenticator) loggedValues[67]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[67] = false;
			loggedValues[67] = null;
		}
		if (logsUsed[53]) {
			try {
				javax.swing.LayoutStyle.setInstance((javax.swing.LayoutStyle) loggedValues[53]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[53] = false;
			loggedValues[53] = null;
		}
		if (logsUsed[39]) {
			try {
				javax.naming.spi.NamingManager.setInitialContextFactoryBuilder((javax.naming.spi.InitialContextFactoryBuilder) loggedValues[39]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[39] = false;
			loggedValues[39] = null;
		}
		if (logsUsed[40]) {
			try {
				javax.naming.spi.NamingManager.setObjectFactoryBuilder((javax.naming.spi.ObjectFactoryBuilder) loggedValues[40]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[40] = false;
			loggedValues[40] = null;
		}
		if (logsUsed[45]) {
			try {
				javax.security.auth.login.Configuration.setConfiguration((javax.security.auth.login.Configuration) loggedValues[45]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[45] = false;
			loggedValues[45] = null;
		}
		if (logsUsed[65]) {
			try {
				javax.swing.JDialog.setDefaultLookAndFeelDecorated((boolean) loggedValues[65]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[65] = false;
			loggedValues[65] = null;
		}
		if (logsUsed[30]) {
			try {
				java.sql.DriverManager.setLoginTimeout((int) loggedValues[30]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[30] = false;
			loggedValues[30] = null;
		}
		if (logsUsed[32]) {
			try {
				java.sql.DriverManager.setLogWriter((java.io.PrintWriter) loggedValues[32]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[32] = false;
			loggedValues[32] = null;
		}
		if (logsUsed[31]) {
			try {
				java.sql.DriverManager.setLogStream((java.io.PrintStream) loggedValues[31]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[31] = false;
			loggedValues[31] = null;
		}
		if (logsUsed[50]) {
			try {
				javax.swing.JOptionPane.setRootFrame((java.awt.Frame) loggedValues[50]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[50] = false;
			loggedValues[50] = null;
		}
		if (logsUsed[49]) {
			try {
				javax.swing.JComponent.setDefaultLocale((java.util.Locale) loggedValues[49]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[49] = false;
			loggedValues[49] = null;
		}
		if (logsUsed[3]) {//try{
			//java.awt.AWTEventMulticaster..(()loggedValues[3]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[3] = false;
			loggedValues[3] = null;
		}
		if (logsUsed[15]) {
			try {
				java.net.ProxySelector.setDefault((java.net.ProxySelector) loggedValues[15]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[15] = false;
			loggedValues[15] = null;
		}
		if (logsUsed[5]) {
			try {
				java.beans.Introspector.setBeanInfoSearchPath((java.lang.String[]) loggedValues[5]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[5] = false;
			loggedValues[5] = null;
		}
		if (logsUsed[36]) {
			try {
				javax.activation.FileTypeMap.setDefaultFileTypeMap((javax.activation.FileTypeMap) loggedValues[36]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[36] = false;
			loggedValues[36] = null;
		}
		if (logsUsed[29]) {//try{
			//java.security.Security.setProperty(()loggedValues[29]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[29] = false;
			loggedValues[29] = null;
		}
		if (logsUsed[4]) {
			try {
				java.awt.KeyboardFocusManager.setCurrentKeyboardFocusManager((java.awt.KeyboardFocusManager) loggedValues[4]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[4] = false;
			loggedValues[4] = null;
		}
		if (logsUsed[26]) {
			try {
				java.security.Policy.setPolicy((java.security.Policy) loggedValues[26]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[26] = false;
			loggedValues[26] = null;
		}
		if (logsUsed[23]) {
			try {
				java.rmi.server.RMISocketFactory.setFailureHandler((java.rmi.server.RMIFailureHandler) loggedValues[23]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[23] = false;
			loggedValues[23] = null;
		}
		if (logsUsed[24]) {
			try {
				java.rmi.server.RMISocketFactory.setSocketFactory((java.rmi.server.RMISocketFactory) loggedValues[24]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[24] = false;
			loggedValues[24] = null;
		}
		if (logsUsed[64]) {
			try {
				javax.swing.text.LayoutQueue.setDefaultQueue((javax.swing.text.LayoutQueue) loggedValues[64]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[64] = false;
			loggedValues[64] = null;
		}
		if (logsUsed[33]) {
			try {
				java.util.Locale.setDefault((java.util.Locale) loggedValues[33]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[33] = false;
			loggedValues[33] = null;
		}
		if (logsUsed[66]) {
			try {
				javax.swing.JFrame.setDefaultLookAndFeelDecorated((boolean) loggedValues[66]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[66] = false;
			loggedValues[66] = null;
		}
		if (logsUsed[7]) {
			try {
				java.lang.System.setOut((java.io.PrintStream) loggedValues[7]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[7] = false;
			loggedValues[7] = null;
		}
		if (logsUsed[8]) {
			try {
				java.lang.System.setIn((java.io.InputStream) loggedValues[8]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[8] = false;
			loggedValues[8] = null;
		}
		if (logsUsed[9]) {
			try {
				java.lang.System.setProperties((java.util.Properties) loggedValues[9]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[9] = false;
			loggedValues[9] = null;
		}
		if (logsUsed[10]) {
			try {
				java.lang.System.setSecurityManager((java.lang.SecurityManager) loggedValues[10]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[10] = false;
			loggedValues[10] = null;
		}
		if (logsUsed[11]) {//try{
			//java.lang.System.setProperty(()loggedValues[11]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[11] = false;
			loggedValues[11] = null;
		}
		if (logsUsed[12]) {
			try {
				java.lang.System.setErr((java.io.PrintStream) loggedValues[12]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[12] = false;
			loggedValues[12] = null;
		}
		if (logsUsed[6]) {
			try {
				java.beans.PropertyEditorManager.setEditorSearchPath((java.lang.String[]) loggedValues[6]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[6] = false;
			loggedValues[6] = null;
		}
		if (logsUsed[62]) {//try{
			//javax.swing.plaf.nimbus.EffectUtils.setPixels(()loggedValues[62]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[62] = false;
			loggedValues[62] = null;
		}
		if (logsUsed[34]) {
			try {
				java.util.TimeZone.setDefault((java.util.TimeZone) loggedValues[34]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[34] = false;
			loggedValues[34] = null;
		}
		if (logsUsed[61]) {
			try {
				javax.swing.UIManager.setInstalledLookAndFeels((javax.swing.UIManager.LookAndFeelInfo[]) loggedValues[61]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[61] = false;
			loggedValues[61] = null;
		}
		if (logsUsed[60]) {
			try {
				javax.swing.UIManager.setLookAndFeel((java.lang.String) loggedValues[60]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[60] = false;
			loggedValues[60] = null;
		}
		if (logsUsed[52]) {//try{
			//javax.swing.KeyboardManager.setCurrentManager((javax.swing.KeyboardManager)loggedValues[52]);}catch(Exception ex){ex.printStackTrace();}
			logsUsed[52] = false;
			loggedValues[52] = null;
		}
		if (logsUsed[48]) {
			try {
				javax.swing.FocusManager.setCurrentManager((javax.swing.FocusManager) loggedValues[48]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[48] = false;
			loggedValues[48] = null;
		}
		if (logsUsed[22]) {
			try {
				java.rmi.server.LogStream.setDefaultStream((java.io.PrintStream) loggedValues[22]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[22] = false;
			loggedValues[22] = null;
		}
		if (logsUsed[25]) {
			try {
				java.rmi.server.RemoteServer.setLog((java.io.OutputStream) loggedValues[25]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logsUsed[25] = false;
			loggedValues[25] = null;
		}

		if (logsUsed[1]) {
			loggedValues[1] = null;
			logsUsed[1] = false;
		}
		if (logsUsed[2]) {
			loggedValues[2] = null;
			logsUsed[2] = false;
		}
		if (logsUsed[27]) {
			loggedValues[27] = null;
			logsUsed[27] = false;
		}
		if (logsUsed[28]) {
			loggedValues[28] = null;
			logsUsed[28] = false;
		}
		if (logsUsed[56]) {
			for (WeakReference<Object> o : (LinkedList<WeakReference<Object>>) loggedValues[56])
				if (!o.isEnqueued())
					javax.swing.UIManager.removePropertyChangeListener((java.beans.PropertyChangeListener) o.get());
			loggedValues[56] = null;
			logsUsed[56] = false;
		}
		if (logsUsed[57]) {
			for (WeakReference<Object> o : (LinkedList<WeakReference<Object>>) loggedValues[57])
				if (!o.isEnqueued())
					javax.swing.UIManager.removeAuxiliaryLookAndFeel((javax.swing.LookAndFeel) o.get());
			loggedValues[57] = null;
			logsUsed[57] = false;
		}
		if (logsUsed[58]) {
			for (Object o : (LinkedList<Object>) loggedValues[58])
				javax.swing.UIManager.addPropertyChangeListener((java.beans.PropertyChangeListener) o);
			loggedValues[58] = null;
			logsUsed[58] = false;
		}
		if (logsUsed[59]) {
			for (Object o : (LinkedList<Object>) loggedValues[59])
				javax.swing.UIManager.addAuxiliaryLookAndFeel((javax.swing.LookAndFeel) o);
			loggedValues[59] = null;
			logsUsed[59] = false;
		}

	}

	public static void logStaticInternal(Object o, int i) {
		if (logsUsed[i])
			return;
		logsUsed[i] = true;
		loggedValues[i] = o;
	}

	@SuppressWarnings("unchecked")
	public static void logStaticInternalAdd(Object o, int i) {
		if (!logsUsed[i])
			loggedValues[i] = new LinkedList<WeakReference<Object>>();
		logsUsed[i] = true;
		((LinkedList<WeakReference<Object>>) loggedValues[i]).add(new WeakReference<Object>(o));
	}

	public static void logStaticInternalRemove(Object o, int i) {
		if (!logsUsed[i])
			loggedValues[i] = new LinkedList<Object>();
		logsUsed[i] = true;
		((LinkedList<Object>) loggedValues[i]).add(o);
	}

	public static String logAndSetProperty(String key, String value) {
		if (!properties.containsKey(key))
			properties.put(key, System.getProperty(key));
		return System.setProperty(key, value);
	}

	public static void logAndSetProperty(Properties values) {
		for (Object key : values.keySet()) {
			if (!properties.containsKey(key))
				properties.put((String) key, System.getProperty((String) key));
		}
		System.setProperties(values);
	}

	public static <T> Constructor<T>[] hideVMVMConstructors(Constructor<T>[] in) {
		int n = 0;
		for (Constructor<T> o : in) {
			boolean hasVMVM = false;
			for (Class<?> c : o.getParameterTypes()) {
				if (c.equals(VMState.class))
					hasVMVM = true;
			}
			if (!hasVMVM)
				n++;
		}
		Constructor<T>[] ret = new Constructor[n];
		n = 0;
		for (Constructor<T> o : in) {
			boolean hasVMVM = false;
			for (Class<?> c : o.getParameterTypes())
				if (c.equals(VMState.class))
					hasVMVM = true;
			if (!hasVMVM) {
				ret[n] = o;
				n++;
			}
		}
		return ret;
	}

	public static int getVMId() {
		Long id = Thread.currentThread().getId();
		if (!threadVMStatus.containsKey(id))
			threadVMStatus.put(id, new VMState(0, 0));
		return threadVMStatus.get(id).getState();
	}

	public static VMState getVMState() {
		Long id = Thread.currentThread().getId();
		if (!threadVMStatus.containsKey(id))
			threadVMStatus.put(id, new VMState(0, 0));
		return new VMState(threadVMStatus.get(id).getState(), threadVMStatus.get(id).getState());
	}

	public static void setVMId(long thread, int id) {
		if (threadVMStatus.get(Thread.currentThread().getId()).getState() == id || threadVMStatus.get(Thread.currentThread().getId()).getState() == 0)
			threadVMStatus.put(thread, new VMState(id, 0));
	}

	public static void setVMed(long thread) {
		int vmId = threadVMStatus.get(Thread.currentThread().getId()).getState();

		VMState state = threadVMStatus.get(thread);
		if (state == null) {
			threadVMStatus.put(thread, new VMState(vmId, vmId));
		} else {
			state.originalVMID = state.vmID;
			state.setState(vmId);
		}
	}

	public static VMState setVMed() {
		int vmId = 0;
		synchronized (nextVM) {
			vmId = nextVM;
			nextVM++;
		}
		VMState state = threadVMStatus.get(Thread.currentThread().getId());
		if (state == null) {
			threadVMStatus.put(Thread.currentThread().getId(), new VMState(vmId, 0));
			return threadVMStatus.get(Thread.currentThread().getId());
		} else {
			state.originalVMID = state.vmID;
			state.setState(vmId);
			return state;
		}
	}

	public static VMState setVMed(int n) {

		VMState state = threadVMStatus.get(Thread.currentThread().getId());
		if (state.getState() == 0) {
			state.originalVMID = state.vmID;
			state.setState(n);
			return state;
		}
		//TODO
		//		System.out.println("OMG returning null we'll just be insecure and let it work");
		//		return null;
		state.originalVMID = state.vmID;
		state.setState(n);
		return state;
	}

	public static void resetStatics() {
		System.err.println("\n\n>>>>>>>>>>>Resetting statics (" + classes.size() + ") <<<<<<<<<<\n\n");
		resetInternalStatics();
		//		new Exception().printStackTrace();
		synchronized (classes) {
			for (String s : properties.keySet()) {
				//				System.out.println("Reseting " + s + " from <" + System.getProperty(s) + "> to <" + properties.get(s) + ">");
				if (properties.get(s) != null)
					System.setProperty(s, properties.get(s));
				else
					System.clearProperty(s);
			}
			properties.clear();

			ArrayList<MBeanServer> mbeanServers = MBeanServerFactory.findMBeanServer(null);
			if (mbeanServers != null && mbeanServers.size() > 0) {
				for (MBeanServer server : mbeanServers) {
					System.out.println("Releasing server " + server);
					MBeanServerFactory.releaseMBeanServer(server);
				}
			}
			mbeanServers = null;
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			if (server.getMBeanCount() > 0) {
				Set<ObjectName> onames = server.queryNames(null, null);
				for (ObjectName name : onames)
					try {
						server.unregisterMBean(name);
					} catch (Throwable e) {
					}
			}
			server = null;
			while (!classesInOrder.isEmpty()) {
				WeakReference<Class<?>> cr = classesInOrder.poll();
				Class<?> c = cr.get();
				classes.remove(cr);
				if (c == null) {
					System.err.println("Skipping something null in reset!");
					continue;
				}
				//					System.err.println("Calling reset on z " + c.getName());
				//				if(!c.getName().contains("tomcat") && ! c.getName().contains("catalina") )
				try {
					//					Method m = c.getMethod(Constants.VMVM_STATIC_RESET_METHOD);
					//					if(!m.isAccessible())
					//						m.setAccessible(true);
					//					m.invoke(null);
					Field f = c.getField(Constants.VMVM_NEEDS_RESET);
					if (!f.isAccessible())
						f.setAccessible(true);
					f.set(null, true);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//				} catch (InvocationTargetException e) {
					//					// TODO Auto-generated catch block
					//					e.printStackTrace();
					//				} catch (NoSuchMethodException e) {
					//					// TODO Auto-generated catch block
					//					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {
					e.printStackTrace();
				}

			}
		}
		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
		//		ChrootUtils.reset();
		//		ThreadGroup thisThreadGroup = Thread.currentThread().getThreadGroup();
		////		while(thisThreadGroup.getParent() != null)
		////			thisThreadGroup = thisThreadGroup.getParent();
		//		Thread[] tt= new Thread[thisThreadGroup.activeCount() + 20];
		//		thisThreadGroup.enumerate(tt, true);
		//		for(Thread t : tt)
		//		{
		//			if(t!= null && t.isAlive() && t != Thread.currentThread())
		//			{
		//				System.err.println("Running thread to kill: " + t + "; cl: " + t.getContextClassLoader());
		//				while(t.isAlive())
		//					t.stop();
		//			}
		//		}
		//		tt= new Thread[thisThreadGroup.activeCount() + 20];
		//		thisThreadGroup.enumerate(tt, true);
		//		for(Thread t : tt)
		//		{
		//			if(t!= null && t.isAlive() && t != Thread.currentThread())
		//			{
		//				System.err.println("Running thread still up: " + t + "; cl: " + t.getContextClassLoader());
		//				while(t.isAlive())
		//					t.stop();
		//			}
		//			else if(t != null)
		//			{
		//				System.err.println("Other thread: " + t);
		//			}
		//		}
		//		
		//		tt= new Thread[thisThreadGroup.activeCount() + 20];
		//		while(thisThreadGroup.getParent() != null)
		//		thisThreadGroup = thisThreadGroup.getParent();
		//		thisThreadGroup.enumerate(tt, true);
		//		for(Thread t : tt)
		//		{
		//			if(t!= null && t.isAlive())
		//			{
		//				System.err.println("Running thread still up: " + t + "; cl: " + t.getContextClassLoader());
		//			}
		//		}
		System.err.println("VR Classloader: " + Thread.currentThread().getContextClassLoader());
	}

	public static class InternalStaticClass {
		HashMap<String, Integer> getMethods = new HashMap<>();
		public HashMap<String, Integer> setMethods = new HashMap<>();
		public HashMap<String, Integer> addMethods = new HashMap<>();
		public HashMap<String, Integer> removeMethods = new HashMap<>();

		@Override
		public String toString() {
			return "InternalStaticClass [getMethods=" + getMethods + ", setMethods=" + setMethods + ", addMethods=" + addMethods + ", removeMethods=" + removeMethods + "]";
		}

	}
}
