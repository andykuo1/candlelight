package org.bstone.util;

import java.util.Objects;

/**
 * Created by Andy on 7/20/17.
 */
public class Pair<A, B> {
	private A fst;
	private B snd;

	public Pair(A var1, B var2) {
		this.fst = var1;
		this.snd = var2;
	}

	public A getFirst()
	{
		return this.fst;
	}

	public B getSecond()
	{
		return this.snd;
	}

	public void set(A fst, B snd)
	{
		this.fst = fst;
		this.snd = snd;
	}

	public String toString() {
		return "Pair[" + this.fst + "," + this.snd + "]";
	}

	public boolean equals(Object var1) {
		return var1 instanceof Pair && Objects.equals(this.fst, ((Pair)var1).fst) && Objects.equals(this.snd, ((Pair)var1).snd);
	}

	public int hashCode() {
		return this.fst == null?(this.snd == null?0:this.snd.hashCode() + 1):(this.snd == null?this.fst.hashCode() + 2:this.fst.hashCode() * 17 + this.snd.hashCode());
	}

	public static <A, B> Pair<A, B> of(A var0, B var1) {
		return new Pair<>(var0, var1);
	}
}