package CurveAnalysisVersion0;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Map;
import java.util.prefs.Preferences;

public class Curve extends Pane {


    private ArrayList<Double> values;
    private double xMin;
    private double xMax;
    private double xInc;
    private CurveAnalysisVersion0.Axes axes;
    private Path path;
    private String[] behaviour = {"", "", "", "", ""};
    private String nullstellen = "";
    double virtewert;

    public Curve(ArrayList<Double> values, double xMin, double xMax, double xInc, CurveAnalysisVersion0.Axes axes) {
        this.values = values;
        this.xMin = xMin;
        this.xMax = xMax;
        this.xInc = xInc;
        this.axes = axes;
        this.path = new Path();

        draw();
        analyseBehaviour();
    }


    private void draw() {

        path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
        path.setStrokeWidth(2);
        path.setClip(
                new Rectangle(
                        0, 0,
                        axes.getPrefWidth(),
                        axes.getPrefHeight()
                )
        );

        double x = xMin;
        double y = calcYValue(x);
        double y1 = 0.9;
        path.getElements().add(
                new MoveTo(
                        mapX(x, axes), mapY(y, axes)
                )
        );
        nullstellen = "";

        x += xInc;
        while (x < xMax) {
            y = calcYValue(x);


            path.getElements().add(
                    new LineTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );
            x += xInc;
        }

        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        getChildren().setAll(path);
    }

    private double mapX(double x, CurveAnalysisVersion0.Axes axes) {
        double tx = axes.getPrefWidth() / 2;
        double sx = axes.getPrefWidth() /
                (axes.getXAxis().getUpperBound() -
                        axes.getXAxis().getLowerBound());
        return x * sx + tx;
    }

    private double mapY(double y, CurveAnalysisVersion0.Axes axes) {
        double ty = axes.getPrefHeight() / 2;
        double sy = axes.getPrefHeight() /
                (axes.getYAxis().getUpperBound() -
                        axes.getYAxis().getLowerBound());

        return -y * sy + ty;
    }

    private double calcYValue(double x) {


        double y = 0;


        //   double y =(values.get(4)*Math.pow(x,4)+values.get(3)*Math.pow(x,3)*+values.get(2)*Math.pow(x,2)+values.get(1)*x+values.get(0));
        // System.out.println(y);
        double multiplyer = 1.;
        //alternativ

        for (double value : values) {
            y += value * multiplyer;
            multiplyer *= x;


        }


        //altanativ3
        /*
        y=values.get(4);
        for (int i=3;i>0;i--){
            y*=x;
            y+=values.get(i);
        }

         */

        return y;
    }

    private void analyseBehaviour() {

        {
        /*
        f(x) = 4x+3x+2x+1x+0
        f(x) = ax+bx³+cx²+dx+e
        Achsensymmetrisch
        b = 0, d = 0,  mindestens ein wert a,c,e != 0
        Punktsymmetrisch
        a,c,e = 0
        mindestens einen wert b,d != 0
         */

        /*
        Nach fragen eigentlich das müsste gehen
        do{
            behaviour[0] = "Aschsensymmetrisch";
        }
        while (values.get(4) != 0 || values.get(2) !=0 || values.get(0) != 0 );

   for (double v:values) {

            }

         */

            behaviour[4] = nullstellen;
            String y, x;

            //  if ((values.get(4)==0 && values.get(3)==0&&values.get(2)!=0)) {

            achsenSymmetreprufen(values);
            double punkt = values.get(4) * Math.pow(0, 4) + values.get(3) * Math.pow(0, 3) + values.get(2) * Math.pow(0, 2) + values.get(1) * 0 + values.get(0) ;
            if (punkt == 0) {
                punktSymmetreprufen(values);
            }

            //   }
            // if ((values.get(4) == 0 && values.get(2) == 0 && values.get(0) == 0) || values.get(3) != 0 || values.get(1) != 0) {
            //  behaviour[0] = "punktsymmetrisch";

            //}
            behaviour[3] = values.get(0).toString();
            if (values.get(4) == 0 && values.get(2) == 0 && values.get(3) == 0 && values.get(1) == 0 && values.get(0) != 0) {
                behaviour[4] = "Hat keine Null Stelle";
            }
            if (values.get(1) != 0 && values.get(0) != 0 && values.get(4) == 0 && values.get(2) == 0 && values.get(3) == 0 ||
                    values.get(1) != 0 && values.get(0) == 0 && values.get(4) == 0 && values.get(2) == 0 && values.get(3) == 0) {


                if (values.get(1) < 0) {
                    double x1 = (values.get(0) / (values.get(1))) * -1;

                    behaviour[4] = "Hat Null Stelle bei " + Math.round(x1 * 100.) / 100.;
                } else if (values.get(1) > 0) {
                    double x2 = values.get(0) / (-values.get(1));
                    behaviour[4] = "Hat Null Stelle bei " + Math.round(x2 * 100.) / 100.;
                }



            }
            if (values.get(4) == 0 && values.get(3) == 0 && values.get(2) != 0) {

                //  pqformelmitnullStelle(values, 0);

                pqformelforzweipunkte(values);
            }
            if (values.get(4) == 0 && values.get(3) != 0) {
                //              behaviour[4] = "Hat 2 null  Stelle";
                nullStelleFürdreierFunktion(values);


                //  nullStellemitInterval(values);
            }
            if (values.get(4) != 0) {
                // behaviour[4] = "Hat 2 null  Stelle";


                nullStelleFürdreierFunktion(gebzuruckdreifunktion(values));

            }


        }


    }

    private void punktSymmetreprufen(ArrayList<Double> values) {
        int xwert = 0;
        boolean weiter = true;
        do {
            xwert++;
            double ywert = values.get(4) * Math.pow(xwert, 4) + values.get(3) * Math.pow(xwert, 3) + values.get(2) * Math.pow(xwert, 2) + values.get(1) * xwert + values.get(0);
            int minusx = xwert * -1;
            double yinandrebereichwert = values.get(4) * Math.pow(minusx, 4) + values.get(3) * Math.pow(minusx, 3) + values.get(2) * Math.pow(minusx, 2) + values.get(1) * minusx + values.get(0);
            // System.out.println(xwert);
            //  System.out.println("ywe" + ywert + "andere :" + yinandrebereichwert);

            if (ywert + yinandrebereichwert != 0 || xwert >= 10) {
                weiter = false;
            } else if (ywert + yinandrebereichwert == 0 && xwert == 9) {
                behaviour[0] = "punktSymmetre";
                // System.out.println("zeigennnnn");

            }

        } while (weiter);


    }

    private void achsenSymmetreprufen(ArrayList<Double> values) {
        int xwert = 0;
        boolean weiter = true;
        do {
            xwert++;
            double ywert = values.get(4) * Math.pow(xwert, 4) + values.get(3) * Math.pow(xwert, 3) + values.get(2) * Math.pow(xwert, 2) + values.get(1) * xwert + values.get(0);
            int minusx = xwert * -1;
            // System.out.println(values.get(2) * Math.pow(minusx, 2));
            double yinandrebereichwert = values.get(4) * Math.pow(minusx, 4) + values.get(3) * Math.pow(minusx, 3) + values.get(2) * Math.pow(minusx, 2) + values.get(1) * minusx + values.get(0);


            if (ywert != yinandrebereichwert || xwert >= 10) {
                weiter = false;
            } else if (ywert == yinandrebereichwert && xwert == 9) {
                behaviour[0] = "Aschsensymmetrisch";

            }

        } while (weiter);


    }

    private ArrayList<Double> gebzuruckdreifunktion(ArrayList<Double> values) {
        ArrayList<Double> werte = new ArrayList<>();
        double wert = -10;
        double s;
        boolean weiter = true;

        do {
            wert += 0.1;
            // System.out.println("wert so :" + wert);

            s = (values.get(4) * Math.pow(wert, 4)) + (values.get(3) * Math.pow(wert, 3)) +
                    (values.get(2) * Math.pow(wert, 2)) + (values.get(1) * wert) + values.get(0);
            System.out.println("werty :\t" + s);
            if (s >= -0.1 && s <= 0.1 || s >= -0.1 && s <= 0.1 || wert == 50) {
                weiter = false;

                //  System.out.println("jaaaaaa");
            } else if (wert > 8) {
                weiter = false;
            }

        } while (weiter);


        if (wert <= 0.0) {

            wert = wert * -1;
        } else {
            wert = wert * -1;

        }
        werte.add(values.get(4));
        if (values.get(3) - (werte.get(0) * wert) == 0) {
            werte.add(values.get(3));
        } else {
            double x = (values.get(3) - (werte.get(0) * wert));
            werte.add(x);


           /*
           while (x!=0){
               if(((i*1)-x)==0){
                   werte.add(i);
                   x=0;
               }else {
               i++;
               }
           }

            */

        }
        if (values.get(2) - (werte.get(1) * (wert)) == 0) {
            werte.add(values.get(2));
        } else {
            double y = (values.get(2) - (werte.get(1) * wert));
            //  System.out.println("werte :" + (values.get(1) - (werte.get(1) * 1)));
            werte.add(y);

        }
        if (values.get(1) - (werte.get(2) * (wert)) == 0) {
            werte.add(values.get(1));
        } else {
            double y = (values.get(1) - (werte.get(2) * wert));
            //  System.out.println("werte :" + (values.get(1) - (werte.get(1) * 1)));
            werte.add(y);
        }
        //System.out.println("werte1 :" + werte.get(2) + " \t  werte2 :" + werte.get(1) + " \t werte3: " + werte.get(0) + "va :" + values.get(3));
        ArrayList<Double> w1 = new ArrayList<>();

        w1.add(Math.round(werte.get(3) * 100.) / 100.);
        w1.add(Math.round(werte.get(2) * 100.) / 100.);
        w1.add(Math.round(werte.get(1) * 100.) / 100.);
        w1.add(Math.round(werte.get(0) * 100.) / 100.);

        // System.out.println("Wert1 :" + w1);


        wert = wert * -1;

        virtewert = wert;
        return w1;

//-0.2, 0.2, 0.0, 0.1
    }

    private void nullStellemitInterval(ArrayList<Double> values) {
        int i = 0;
        double x = 0;
        double x1 = 1;
        double nullstelle = 0;
        do {
            double wert = (values.get(3) * Math.pow(x, 3)) + (values.get(2) * Math.pow(x, 2)) + (values.get(1) * x) + values.get(0);
            double wert1 = (values.get(3) * Math.pow(x1, 3)) + (values.get(2) * Math.pow(x1, 2)) + (values.get(1) * x1) + values.get(0);
            x++;
            x1++;
            //   System.out.println("wert2 von anfang bei x:" + x1 + " ist " + wert1 + "wert von annfang  bei x1" + x + "ist :" + wert);
            if (wert > 0 && wert1 < 0 || wert < 0 && wert1 > 0) {
                double welchewertminus = 0;
                double welchewertpositve = 0;
                if (wert < 0) {
                    welchewertminus = x;
                    //   System.out.println("1" + x);
                } else {
                    welchewertpositve = x;
                    System.out.println("2" + x);
                }
                if (wert1 < 0) {
                    welchewertminus = x1;
                    //   System.out.println("3" + x1);
                } else {
                    welchewertpositve = x1;
                    //  System.out.println("4" + x1);
                }
                boolean finish = true;
                double x2 = (x + x1) / 2;
                int y = 0;
                do {

                    double wert2 = (values.get(3) * Math.pow(x2, 3)) + (values.get(2) * Math.pow(x2, 2)) + (values.get(1) * x2) + values.get(0);
                    //  System.out.println("wert2 unten bei x=" + x2 + " :" + wert2);
                    if (wert == -0.004 || wert == 0.004) {
                        nullstelle = x2;
                        finish = false;
                    } else if (wert2 > 0.0) {
                        x2 = (welchewertminus + x2) / 2;
                        //   System.out.println("wert1 x2 unten bei x=" + x2 + "welche" + welchewertminus);
                    } else if (wert2 < 0.0) {
                        x2 = (welchewertpositve + x2) / 2;
                        //  System.out.println("wert2 x2 unten bei x=" + x2 + "welche" + welchewertminus);
                    }
                    y++;
                } while (finish);

            }
            i++;


        } while (i < 8);
        behaviour[4] = "bei :" + (1 - nullstelle);
    }

    private void nullStelleFürdreierFunktion(ArrayList<Double> values) {
        ArrayList<Double> werte = new ArrayList<>();
        double wert = -10;
        double s;
        boolean weiter = true;
        boolean gehzwite = false;
        // System.out.println("meth : pppppppppppppppppppppppp"+gebzuruckdreifunktion(values));
        do {
            wert += 0.1;
            //   System.out.println("wert so :" + wert);

            s = (values.get(3) * Math.pow(wert, 3)) + (values.get(2) * Math.pow(wert, 2)) + (values.get(1) * wert) + values.get(0);
            //  System.out.println("wert :" + s);
            if (s >= -0.1 && s <= 0.1 || s >= -0.1 && s <= 0.1 || wert == 50) {
                weiter = false;
                gehzwite = true;
                //  System.out.println("jaaaaaa");
            } else if (wert > 8) {
                weiter = false;
            }

        } while (weiter);
        /*
        if(gehzwite==false) {
            boolean weit=true;
            wert=0;
            do {
                wert += -0.1;
                System.out.println("wert so unten \t :" + wert);

                s = (values.get(3) * Math.pow(wert, 3)) + (values.get(2) * Math.pow(wert, 2)) + (values.get(1) * wert) + values.get(0);
                System.out.println("wert :" + s);
                if (s >= -0.1 && s <= 0.1 || s >= -0.1 && s <= 0.1 || wert == 50) {
                 weit= false;

                    //  System.out.println("jaaaaaa");
                } else if (wert < -8) {
                    weit= false;
                }
            } while (weit);
        }
        */
        int vorzeichne;

        if (wert <= 0.0) {
            vorzeichne = -1;
            wert = wert * -1;
        } else {
            wert = wert * -1;

        }
        werte.add(values.get(3));
        if (values.get(2) - (werte.get(0) * wert) == 0) {
            werte.add(values.get(2));
        } else {
            double i = 0;
            double x = (values.get(2) - (werte.get(0) * wert));
            werte.add(x);


           /*
           while (x!=0){
               if(((i*1)-x)==0){
                   werte.add(i);
                   x=0;
               }else {
               i++;
               }
           }

            */

        }
        if (values.get(1) - (werte.get(1) * (wert)) == 0) {
            werte.add(werte.get(0));
        } else {
            double y = (values.get(1) - (werte.get(1) * wert));
            //  System.out.println("werte :" + (values.get(1) - (werte.get(1) * 1)));
            werte.add(y);

        }
        //System.out.println("werte1 :" + werte.get(2) + " \t  werte2 :" + werte.get(1) + " \t werte3: " + werte.get(0) + "va :" + values.get(3));
        ArrayList<Double> w1 = new ArrayList<>();
        w1.add(Math.round(werte.get(2) * 100.) / 100.);
        w1.add(Math.round(werte.get(1) * 100.) / 100.);
        w1.add(Math.round(werte.get(0) * 100.) / 100.);
        // System.out.println("Wert1 :" + w1);

            wert = wert * -1;


        pqformelmitnullStelle(w1, wert);
    }

    private void pqformelforzweipunkte(ArrayList<Double> values) {
        String y = "";
        double p, q;

        p = values.get(1) / values.get(2);
        //    System.out.println("das ist p :" + p);
        q = values.get(0) / values.get(2);
        p = Math.round(p * 100.) / 100.;
        q = Math.round(q * 100.) / 100.;
        //  System.out.println("das ist p :" + p);
        // System.out.println("das ist wurzel :" + (Math.pow((p / 2), 2) - (q)));
        if ((Math.pow((p / 2), 2) - (q)) > 0) {
            double x = -(p / 2) + Math.sqrt(Math.pow((p / 2), 2) - (q));
            double x1 = -(p / 2) - Math.sqrt(Math.pow((p / 2), 2) - (q));
            y = "bei x1 :\t" + Math.round(x * 100.) / 100. + "\nbei x2 :\t" + Math.round(x1 * 100.) / 100.;
        } else if (p != 0 && values.get(0) != 0 || p == 0 && values.get(0) != 0) {

            y = "keine null";
        } else {
            y = "" + (-p / 2);
        }

        if (y.equals("")) {
            behaviour[4] = "keine null stelle : ";
        } else {
            behaviour[4] = "Hat Null Stelle bei :" + y;
        }


    }

    private void pqformelmitnullStelle(ArrayList<Double> values, double vor) {
        String y = "";
        double p, q;
        //  System.out.println("vor wert :" + vor);
        p = values.get(1) / values.get(2);
        //     System.out.println("das ist p :" + p);
        q = values.get(0) / values.get(2);
        p = Math.round(p * 100.) / 100.;
        q = Math.round(q * 100.) / 100.;
        // System.out.println("das ist p :" + p);
        //System.out.println("das ist wurzel :" + (Math.pow((p / 2), 2) - (q)));
        if ((Math.pow((p / 2), 2) - (q)) > 0) {
            double x1 = -(p / 2) + Math.sqrt(Math.pow((p / 2), 2) - (q));
            double x2 = -(p / 2) - Math.sqrt(Math.pow((p / 2), 2) - (q));
            y = "bei x1 \t:" + Math.round(x1 * 100.) / 100. + " \nund bei x2 \t:" + Math.round(x2 * 100.) / 100.;
        } else {
            y = "bei :\t" + (-p / 2);
        }
        //}else {
        /*
            y= ""+((-p/2));
            System.out.println("das ist p :"+y);
            behaviour[4]=y;
            
         */
        //}

        /*
        System.out.println("p wurzel:" + Math.sqrt(Math.pow((p / 2), 2) - (q)));
        System.out.println("p wurzel1:" +  Math.sqrt(Math.pow((p / 2), 2) - (q)));
        System.out.println(y);

         */


            if (vor != 0) {
                y += " und Hat Null Stelle bei  \n und bei \t:" + Math.round(vor * 100.) / 100.;
                if (virtewert != 0) {

                   y+=  "\n und bei : \t" + virtewert ;

                }

                behaviour[4] = "Hat Null Stelle bei :" + y;


        }
        //     System.out.println("vier" + virtewert);


    }

    public String getBehaviour(int index) {
        if (index >= 0 && index <= 4) {
            return behaviour[index];
        } else {
            return "none";
        }
    }

    public void setFunction(ArrayList<Double> values) {
        this.values = values;
        draw();
    }
}
