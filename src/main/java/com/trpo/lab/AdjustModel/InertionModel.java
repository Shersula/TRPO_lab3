package com.trpo.lab.AdjustModel;

public class InertionModel implements Adjustable {

	private long Time = System.currentTimeMillis();

	private float EnergyLoss;
	private float Energy = -EnergyLoss;

	private float Force = 0;
	private float Value;

	public InertionModel(float EnergyLoss)
	{
		this.EnergyLoss = EnergyLoss;
	}

	@Override
	public void Update()
	{
		float dT = (System.currentTimeMillis()-Time)/1000f;

		Energy += (Force-EnergyLoss)*dT;

		if(Energy < -EnergyLoss) Energy = -EnergyLoss;
		else if(Force > 0 && Energy > Force) Energy = Force;
		
		Value += Energy;
		if(Value < 0) Value = 0;

		Time = System.currentTimeMillis();
	}

	@Override
	public void setForce(float Force) {
		this.Force = Force;
	}

	@Override
	public float getValue() {
		return Value;
	}

}
