package com.teej107.executorhandler;

import java.lang.annotation.*;

/**
 * @author teej107
 * @since Oct 25, 2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Executable
{
	String NO_PERMS_MESSAGE = "&cI\'m sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";
	String OBJECT = "@";
	String VAR_ARG = "@...";

	String COMMAND_SENDER = "<command_sender>";
	String COMMAND = "<command>";

	String command();

	String permission() default "";

	String wrongSenderTypeMessage() default "&cOnly " + COMMAND_SENDER + " can run this command";

	String usage() default "";

	//String description() default "";

	//String[] aliases() default {};

	String noPermissionMessage() default NO_PERMS_MESSAGE;

}
