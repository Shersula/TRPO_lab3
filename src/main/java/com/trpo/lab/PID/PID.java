package com.trpo.lab.PID;

import java.util.concurrent.TimeUnit;

import com.trpo.lab.AdjustModel.Adjustable;

public class PID implements Runnable
{
	private float MaxRegulated;
	private float MinRegulated;
	private Adjustable Model;
	private long Time = System.currentTimeMillis();
	private float P;
	private float I;
	private float D;
	private float kP;
	private float kI;
	private float kD;
	private float Target;

	public PID(Adjustable Model, float Target, float MaxRegulated, float MinRegulated, float kP, float kI, float kD)
	{
		this.MaxRegulated = MaxRegulated;
		this.MinRegulated = MinRegulated;
		this.Model = Model;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.Target = Target;
	}

	private void Calculate()
	{
		float err = Target - Model.getValue();
		float dT = (System.currentTimeMillis() - Time)/1000f;

		D = (err - P)/dT;
		I += err*dT;
		P = err;
	}

	public void setTarget(float target) {
		System.out.println("Target change to " + target);
		Target = target;
	}

	public float getRegulatedForce()
	{
		float Value = (P*kP) + (I*kI) + (D*kD);

		if(Value > MaxRegulated) Value = MaxRegulated;
		else if(Value < MinRegulated) Value = MinRegulated;

		return Value;
	}

	@Override
	public void run() {
		while(!Thread.interrupted())
		{
			Time = System.currentTimeMillis();
			System.out.println(this);

			try
			{
				TimeUnit.MILLISECONDS.sleep(500);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}

			Model.Update();

			Calculate();
			Model.setForce(getRegulatedForce());
		}
	}

	@Override
	public String toString() {
		return String.format("Model: %f\nValue: %f\nP: %f\nI: %f\nD: %f\n", Model.getValue(), getRegulatedForce(), P*kP, I*kI, D*kD);
	}
}
