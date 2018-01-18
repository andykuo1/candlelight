package org.bstone.poma.motivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andy on 1/18/18.
 */
public class PomaTest
{
	public static final Logger logger = LoggerFactory.getLogger("boo!");

	public static void main(String[] args)
	{
		logger.info("Hello!");
		logger.info("Hello!");
		logger.warn("Hello!dfjakshdfakjsdhfkljashdflkjahsdkjfhalkjsdhkdsfl;gkjsdlfkjgs;ldkfjg;lksjdf;ladksjfal;skdjf;laksdjf;lkasdjfl;kajsdfl;kajsd;lfkja;lkdsfj;flkajhsdfakjdhsfkla");
		logger.trace("Hello!");
		logger.error("Hello!");
		logger.debug("Hello!");
		System.exit(0);
	}
}
