package com.prance.lib.teacher.base.http;

/**
 * Created by shenbingbing on 16/4/22.
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
