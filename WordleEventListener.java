/*
 * File: WordleEventListener.java
 * ------------------------------
 * This file defines an interface that is used to specify event
 * callbacks in the Wordle project.
 */

 

/**
 * This interface defines the signature for callback functions in the
 * Wordle assignments.  The easiest way to use this interface  is to use
 * arrow functions, as in the example from the starter project, which
 * passes the arrow function <code>(s) -&gt; enterAction(s)</code>
 * to <code>addEnterListener</code>.  If students have not seen arrow
 * functions, they can also import this interface and define an explicit
 * listener method that implements it.
 */

public interface WordleEventListener {

/*
 * This function defines WordleEventListener as a functional interface,
 * suitable for use with arrow functions.
 */

    public void eventAction(String s);

}
