public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    public static Planet[] readPlanets(String fileName) {
    	In in = new In(fileName);
    	int numPlanets = in.readInt();
    	double radius = in.readDouble();
    	Planet[] planets = new Planet[numPlanets];

    	for (int i = 0; i < numPlanets; i++) {
    		double xPos = in.readDouble();
    		double yPos = in.readDouble();
    		double xVel = in.readDouble();
    		double yVel = in.readDouble();
    		double mass = in.readDouble();
    		String image = in.readString();
    		planets[i] = new Planet(xPos, yPos, xVel, yVel, mass, image);
    	}
    	return planets;
    }

    public static void main(String[] args) {

    	double T = Double.parseDouble(args[0]);
    	double dt = Double.parseDouble(args[1]);
    	String fileName = args[2];
    	double radius = readRadius(fileName);
    	Planet[] planets = readPlanets(fileName);
    	int numPlanets = planets.length;
    	StdDraw.setScale(-radius, radius);
    	StdDraw.picture(0,0,"images/starfield.jpg");
    	
    	for (int i = 0; i < numPlanets; i++) {
    		planets[i].draw();
    	}
    	
    	StdDraw.enableDoubleBuffering();
    	double time = 0;

    	while(time != T){
    		double[] xForces = new double[numPlanets];
    		double[] yForces = new double[numPlanets];
    		for (int i = 0; i < numPlanets; i++) {
    			xForces[i] = planets[i].calcNetForceExertedByX(planets);
    			yForces[i] = planets[i].calcNetForceExertedByY(planets);
       		}
       		
       		for (int i = 0; i < numPlanets; i++) {
       			planets[i].update(dt, xForces[i], yForces[i]);
       		}
       	
    		StdDraw.picture(0,0,"images/starfield.jpg");
    		
    		for (int i = 0; i < numPlanets; i++) {
    			planets[i].draw();
    		}
    		StdDraw.enableDoubleBuffering();
    		StdDraw.show();
    		StdDraw.pause(10);
    		time += dt;
    	}

    	StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i += 1) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
				planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
				planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
		}
    }
}
