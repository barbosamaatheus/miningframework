package br.ufpe.cin.mergers.util;

import java.io.File;

import br.ufpe.cin.app.JFSTMerge;

/**
 * Class representing a textual merge conflict.
 * @author Guilherme
 */

public class MergeConflict {

	public String left;
	public String base;
	public String right;
	public String body;
	
	public int startLOC;
	public int endLOC;
	
	public File leftOriginFile;
	public File baseOriginFile;
	public File rightOriginFile;
	
	public String fullyQualifiedMergedClass;

	public MergeConflict(String leftConflictingContent, String baseConflictingContent, String rightConflictingContent) {
		this.left = leftConflictingContent;
		this.base = baseConflictingContent;
		this.right = rightConflictingContent;

		StringBuilder body = new StringBuilder();
		body.append("<<<<<<< MINE\n").append(leftConflictingContent);
		if(JFSTMerge.showBase) {
			body.append("\n||||||| BASE\n").append(baseConflictingContent);
		}
		body.append("\n=======\n").append(rightConflictingContent).append("\n>>>>>>> YOURS");
		
		this.body = body.toString();
	}

	public MergeConflict(String leftConflictingContent,	String rightConflictingContent) {
		this.left  = leftConflictingContent;
		this.right = rightConflictingContent;
		this.body  ="<<<<<<< MINE\n"+
				    leftConflictingContent+
				    "\n=======\n"+
				    rightConflictingContent+
				    "\n>>>>>>> YOURS";
	}
	
	public MergeConflict(String leftConflictingContent,	String rightConflictingContent, int startLOC, int endLOC) {
		this.left  = leftConflictingContent;
		this.right = rightConflictingContent;
		this.body  ="<<<<<<< MINE\n"+
				    leftConflictingContent+
				    "=======\n"+
				    rightConflictingContent+
				    "\n>>>>>>> YOURS";
		this.startLOC = startLOC;
		this.endLOC = endLOC;
	}
	
	// public MergeConflict(String leftConflictingContent,	String rightConflictingContent, String message) {
	// 	this.left  = leftConflictingContent;
	// 	this.right = rightConflictingContent;
	// 	this.body  ="<<<<<<< MINE\n"+
	// 			    leftConflictingContent+
	// 			    "======= "+ message + " \n" +
	// 			    rightConflictingContent+
	// 			    ">>>>>>> YOURS";
	// }
	
	public boolean contains(String leftPattern, String rightPattern){
		if(leftPattern.isEmpty() || rightPattern.isEmpty()){
			return false;
		} else {
			leftPattern  = (leftPattern.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			rightPattern = (rightPattern.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			String lefttrim  = (this.left.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			String righttrim = (this.right.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			return (lefttrim.contains(leftPattern) && righttrim.contains(rightPattern));
		}
	}
	
	public void setOriginFiles(File left, File base, File right){
		this.leftOriginFile = left;
		this.rightOriginFile = right;
		this.baseOriginFile = base;
	}
	
	public String getFullyQualifiedMergedClass() {
		return fullyQualifiedMergedClass;
	}

	public void setFullyQualifiedMergedClass(String fullyQualifiedMergedClass) {
		this.fullyQualifiedMergedClass = fullyQualifiedMergedClass;
	}
	
	
	@Override
	public String toString() {
		return this.body;
	}
	
/*	public boolean containsRelaxed(String leftPattern, String rightPattern){
		if(leftPattern.isEmpty() || rightPattern.isEmpty()){
			return false;
		} else {
			leftPattern  	 = (leftPattern.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			rightPattern 	 = (rightPattern.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			String lefttrim  = (this.left.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			String righttrim = (this.right.replaceAll("\\r\\n|\\r|\\n","")).replaceAll("\\s+","");
			
			leftPattern 	= Util.removeReservedKeywords(leftPattern);
			rightPattern 	= Util.removeReservedKeywords(rightPattern);
			lefttrim 		= Util.removeReservedKeywords(lefttrim);
			righttrim 		= Util.removeReservedKeywords(righttrim);
			
			return (lefttrim.contains(leftPattern) && righttrim.contains(rightPattern));
		}
	}*/
}
