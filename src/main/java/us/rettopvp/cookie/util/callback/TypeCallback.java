package us.rettopvp.cookie.util.callback;

import java.io.Serializable;

public interface TypeCallback<T> extends Serializable
{
    void callback(final T data);
}
