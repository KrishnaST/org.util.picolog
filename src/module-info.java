/**
 * @author krishna.telgave
 * @version 1.0
 * @since 2019-01-05
 */
module org.util.nanolog {

	exports org.util.nanolog;

	requires static transitive okhttp3;
	requires static transitive okio;
	requires static transitive kotlin.stdlib;
	requires static transitive kotlin.stdlib.common;
}
