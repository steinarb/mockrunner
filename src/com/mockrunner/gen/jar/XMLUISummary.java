package com.mockrunner.gen.jar;


import java.io.*;
import java.util.*;
import com.kirkk.analyzer.framework.*;

/* 
 * Modified version of XMLUISummary
 * http://www.kirkk.com/wiki/wiki.php/Main/JarAnalyzer
 * 
 * This class takes the input directory and output file
 * from the command line or from System.in. It is also
 * usable directly from Java and can be called from
 * an ant task
 */
public class XMLUISummary implements Summary {
	private PrintWriter writer;

	public static void main(String args[]) throws Exception {
		new XMLUISummary().instanceMain(args);
	}

	public void instanceMain(String args[]) throws Exception {
        File scrDir = (args.length > 0) ? new File(args[0]) : null;
        File destFile = (args.length > 1) ? new File(args[1]) : null;
        if(scrDir == null)
        {
            System.out.print("Please enter input directory name: ");
            scrDir = getFile();
        }
        if(destFile == null)
        {
            System.out.print("Please enter output file name: ");
            destFile = getFile();
        }
        createSummary(scrDir, destFile);
	}
    
    public void createSummary(File srcDir, File destFile) throws Exception
    {
        Analyzer analyzer = new Analyzer();
        JarBundle jarBundle[] = analyzer.analyze(srcDir);
        outputAll(jarBundle, destFile);
    }

    private File getFile() throws IOException {
	    try {
		    InputStreamReader streamReader = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(streamReader);
			String fileName = reader.readLine();
			File file = new File(fileName);
			return file;
		} catch (IOException e) {
			throw e;
		}
	}
  
	private void outputAll(JarBundle[] jarBundle, File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			writer = new PrintWriter(fileWriter);
		} catch (IOException e) {
			System.out.println("IOException - Redirecting to System.out");
			System.out.println(e);
			OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
			writer = new PrintWriter(outputWriter);
		}
		this.printHeader();
		this.output(jarBundle);
		this.printFooter();
		writer.flush();
		writer.close();
	}

	private void printHeader() {
		writer.println("<?xml version=\"1.0\"?>");
		writer.println("<JarAnalyzer>");
	}

	private void printFooter() {
		writer.println("</JarAnalyzer>");
	}

	private void output(JarBundle[] jarBundles) {
		writer.println(tab()+"<Jars>");writer.println();
		for (int i = 0; i < jarBundles.length; i++) {
			String jar = jarBundles[i].getJarFileName().substring(jarBundles[i].getJarFileName().lastIndexOf("\\") + 1,
									jarBundles[i].getJarFileName().length());
			writer.println(tab(2)+"<Jar name=\"" + jar + "\">");
			writer.println(tab(3)+"<Summary>");
			this.statistics(jarBundles[i]);writer.println();
			this.jarPackages(jarBundles[i]);writer.println();
			this.externalJars(jarBundles[i]);
			this.unresolveableDependencies(jarBundles[i]);
			writer.println(tab(3)+"</Summary>");writer.println();
			writer.println(tab(2)+"</Jar>");writer.println();
		}
		writer.println(tab()+"</Jars>");
	}

	private void statistics(JarBundle jarBundle) {
		writer.println(tab(4)+"<Statistics>");
		writer.println(tab(5)+"<ClassCount>" + jarBundle.getPublicClassCount() + "</ClassCount>");
		writer.println(tab(5)+"<PackageCount>" + jarBundle.getPackageCount() + "</PackageCount>");
		writer.println(tab(4)+"</Statistics>");
	}
	private void externalJars(JarBundle jarBundle) {
		writer.println(tab(4)+"<ResolvedDependencies>");
		Iterator jarDependencies = jarBundle.getDependentJars().iterator();
		while (jarDependencies.hasNext()) {
			JarBundle dependentBundle = (JarBundle) jarDependencies.next();
			//String jar2 = dependentBundle.getJarFileName().substring(dependentBundle.getJarFileName().lastIndexOf("\\") + 1,
			//												dependentBundle.getJarFileName().length());
			String jar2= dependentBundle.getJarFileName();
			writer.println(tab(5)+"<Service>"+jar2+"</Service>");
		}
		writer.println(tab(4)+"</ResolvedDependencies>");writer.println();
	}


	private void externalDependencies(JarBundle jarBundle) {
		Iterator allPackages = jarBundle.getAllExternallyReferencedPackages().iterator();
		while (allPackages.hasNext()) {
			String javaPackage = (String) allPackages.next();
			writer.println(tab(5)+"<Package>"+javaPackage+"</Package>");
		}
	}

	private void unresolveableDependencies(JarBundle jarBundle) {
		writer.println(tab(4)+"<UnresolvedDependencies>");
		Iterator unresolvedPackages = jarBundle.getAllUnidentifiableExternallyReferencedPackages().iterator();
		while (unresolvedPackages.hasNext()) {
			String packageName = (String) unresolvedPackages.next();
			writer.println(tab(5)+"<Package>"+packageName+"</Package>");
		}
		writer.println(tab(4)+"</UnresolvedDependencies>");
	}

	private void jarPackages(JarBundle jarBundle) {
		writer.println(tab(4)+"<Packages>");
		Iterator allPackages = jarBundle.getAllContainedPackages().iterator();
		while (allPackages.hasNext()) {
			JavaPackage javaPackage = (JavaPackage) allPackages.next();
			writer.println(tab(5)+"<Package>" + javaPackage.getLongName() + "</Package>");
		}
		writer.println(tab(4)+"</Packages>");
	}

	private String tab() {
		return "    ";
	}

	private String tab(int i) {
		String tab = tab();
		for (int j = 0; j < i - 1; j++) {
			tab = tab + tab();
		}
		return tab;
	}

}