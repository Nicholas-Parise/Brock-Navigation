package ca.brocku.MazeSolver;

import java.io.Serializable;

/** Used to box a primitive to be used by more than one Thread
 *
 * @param <T>
 */
public class SharedVariable<T> implements Serializable {
    T data;
    public SharedVariable(T data){
        this.data = data;
    }
    public synchronized T getData() {
        return data;
    }

    public synchronized void setData(T data){
        this.data = data;
    }
}
