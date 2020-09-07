
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.util.Properties;

public class Pipeline {
	private static Properties properties;
	private static String propertiesName = "tokenize, ssplit, pos, lemma, ner,parse,sentiment";
	private static StanfordCoreNLP stanfordCoreNLP;
	
	static {
		properties = new Properties();
		properties.setProperty("annotators", propertiesName);
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		TreebankLanguagePack tlp = lp.treebankLanguagePack();
		TokenizerFactory factory = tlp.getTokenizerFactory();
		factory.setOptions("untokenizable=noneDelete");
	}
	
	public static StanfordCoreNLP getPipeline() {
		
		if(stanfordCoreNLP == null) {
			stanfordCoreNLP = new StanfordCoreNLP(properties);
		}
		return stanfordCoreNLP;
	}
}
