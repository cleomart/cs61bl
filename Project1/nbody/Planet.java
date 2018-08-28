public class Planet{
	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass;
	String imgFileName;
	private static final double G =  6.67e-11;

	/** Constructor that initialize the position, velocity, mass and image of
	this planet */
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	/** Constructor that makes a copy of the Planet P */
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;


	}
	
	/** Calculates the ditance between this planet and Planet p */
	public double calcDistance(Planet p) {
		return Math.pow(Math.pow(this.xxPos- p.xxPos, 2) + Math.pow(this.yyPos - p.yyPos, 2), 0.5);
	}

	/** Calculates the force exerted on this planet by the planet p */
	public double calcForceExertedBy(Planet p){
		return G * this.mass * p.mass / Math.pow(this.calcDistance(p), 2); 
	}


	/** Calculates the force exerted in the X diretion on this planet by the planet p */
	public double calcForceExertedByX(Planet p){
		return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
	}

	/** Calculates the force exerted in the Y diretion on this planet by the planet p */
	public double calcForceExertedByY(Planet p){
		return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);

	}

	/** Calculates the net X force exerted ny all the planets in the array */
	public double calcNetForceExertedByX(Planet[] planets){
		double netForce = 0;
		int index = 0;
		while(index < planets.length){
			if (!this.equals(planets[index])){
				netForce += calcForceExertedByX(planets[index]);
			}
			index++;
		}
		return netForce;
	}

	/** Calculates the net Y force exerted ny all the planets in the array */
	public double calcNetForceExertedByY(Planet[] planets){
		double netForce = 0;
		int index = 0;
		while(index < planets.length){
			if (!this.equals(planets[index])){
				netForce += calcForceExertedByY(planets[index]);
			}
			index++;
		}
		return netForce;
	}

	/** Updates this planet's position and velocity */
	public void update(double dt, double fX, double fY){
		xxVel = xxVel + (dt * fX / mass);
		yyVel = yyVel + (dt * fY / mass);
		xxPos = xxPos + (dt * xxVel);
		yyPos = yyPos + (dt * yyVel);
	}
	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}