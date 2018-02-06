package ru.torment.shared;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import ru.torment.client.GameField;
import ru.torment.client.Util;

public class Unit extends AdvanceSprite implements Serializable
{
	private static final long serialVersionUID = 1L;

	private UUID     id;
	private UnitType unitType;
	private String   name;
	private Color    color;
	private Double   coordX;
	private Double   coordY;
//	private Integer  width;
//	private Integer  height;
	private Integer  speed;
	private Boolean  isSelected;
	private Double   targetCoordX;
	private Double   targetCoordY;
	private TargetQuarter targetQuarter;
	private Double   targetYtoX;
	private Integer  healthPoints;
	private Integer  attack;
	private Integer  attackRadius;
	private Boolean  isMoving;
	private Boolean  isDead;

	private transient Shape shape;

	//======================================================================================
	public Unit( BufferedImage[] bufferedImages, UnitType unitType, String name, Color color, Double coordX, Double coordY, Integer width, Integer height, Integer speed )
	{
		super( bufferedImages, coordX, coordY );

		System.out.println(" + GameClient::Unit::Unit()");

		id = UUID.randomUUID();
		this.unitType = unitType;
		this.name     = name;
		this.color    = color;
		this.coordX   = coordX;
		this.coordY   = coordY;
		this.width    = width;
		this.height   = height;
		this.speed    = speed;
		isSelected = false;
		healthPoints = 100;
		attack = 5;
		attackRadius = 50;
		isMoving = false;
		isDead = false;

//		if ( getUnitType().equals( UnitType.TANK ) )
//		{
//			shape = new Ellipse2D.Double( getCoordX() - getWidth()/2, getCoordY() - getHeight()/2, getWidth(), getHeight() );
//		}
//		else if ( getUnitType().equals( UnitType.BMP ) )
//		{
//			shape = new Rectangle2D.Double( getCoordX() - getWidth()/2, getCoordY() - getHeight()/2, getWidth(), getHeight() );
//		}
//		else if ( getUnitType().equals( UnitType.BTR ) )
//		{
//			shape = new Rectangle2D.Double( getCoordX() - getWidth()/2, getCoordY() - getHeight()/2, getWidth(), getHeight() );
//		}
	}

	//======================================================================================
	public void update( long elapsedTime )
	{
//		System.out.println(" + GameClient::Unit::update()");
		
		// Если объекту указана новая точка
		if ( isMoving() && getTargetCoordX() != null && getTargetCoordY() != null )
		{
			System.out.println(" + GameClient::Unit::update() --- moveToTarget");

			// Если добрался до точки назначения
			// TODO Сделать по-другому
			if ( getTargetCoordX() < getCoordX() + 5 &&
				 getTargetCoordX() > getCoordX() - 5 &&
				 getTargetCoordY() < getCoordY() + 5 &&
				 getTargetCoordY() > getCoordY() - 5 )
			{
				setTargetCoordX(null);
				setTargetCoordY(null);
				setIsMoving(false);
			}

			setMoveAnimation();

			Integer sign = 1;
			Double YtoX = 1D;
			if ( getTargetQuarter().equals( TargetQuarter.II_Quarter     ) ||
				 getTargetQuarter().equals( TargetQuarter.III_Quarter    ) ||
				 getTargetQuarter().equals( TargetQuarter.On_Y_Axis_Left ) ) { sign = -1; }
			if ( getTargetYtoX() > 1 ) { YtoX = 1/getTargetYtoX(); }
			setCoordX( getCoordX() + sign * getSpeed() * YtoX );

			sign = 1;
			YtoX = 1D;
			if ( getTargetQuarter().equals( TargetQuarter.I_Quarter    ) ||
				 getTargetQuarter().equals( TargetQuarter.II_Quarter   ) ||
				 getTargetQuarter().equals( TargetQuarter.On_X_Axis_Up ) ) { sign = -1; }
			if ( getTargetYtoX() < 1 ) { YtoX = getTargetYtoX(); }
			setCoordY( getCoordY() + sign * getSpeed() * YtoX );
		}

//		elapsedTime = System.currentTimeMillis() - time_old;
//		System.out.println(" + GameClient::Unit::update() --- moveToTarget --- elapsedTime: " + elapsedTime );
		super.update( elapsedTime );
//		time_old = 0L;
	}
//	long time_old = 0L;

	//======================================================================================
	public void render( Graphics2D g2d )
	{
//		System.out.println(" + GameClient::Unit::render()");

//		if ( shape instanceof Rectangle2D )
//		{
//			Rectangle2D rectangle2d = (Rectangle2D)shape;
//			rectangle2d.setFrame( getCoordX() - getWidth()/2, getCoordY() - getHeight()/2, getWidth(), getHeight() );
//		}
//		else if ( shape instanceof Ellipse2D )
//		{
//			Ellipse2D ellipse2d = (Ellipse2D)shape;
//			ellipse2d.setFrame( getCoordX() - getWidth()/2, getCoordY() - getHeight()/2, getWidth(), getHeight() );
//		}
//		g2d.setColor( getColor() );
//		g2d.fill( shape );

		super.render( g2d, getCoordX().intValue() - getWidth()/2, getCoordY().intValue() - getHeight()/2 );
	}

	//======================================================================================
	public void checkInteraction()
	{
//		System.out.println(" + GameClient::Unit::checkInteraction()");

		// Проверка досягаемости других объектов относительно текущего
		List<Unit> list_EnemyUnits = GameField.getEnemysInKillZone( this );
		for ( Unit unit_Enemy : list_EnemyUnits )
		{
			unit_Enemy.setHealthPoints( unit_Enemy.getHealthPoints() - getAttack() );
			if ( unit_Enemy.getHealthPoints() <= 0 )
			{
				unit_Enemy.setIsDead(true);
			}
		}
	}

	//======================================================================================
	// Getters
	//======================================================================================
	public UUID     getId()       { return id;         }
	public UnitType getUnitType() { return unitType;   }
	public String   getName()     { return name;       }
	public Color    getColor()    { return color;      }
	public Double   getCoordX()   { return coordX;     }
	public Double   getCoordY()   { return coordY;     }
//	public Integer  getWidth()    { return width;      }
//	public Integer  getHeight()   { return height;     }
	public Integer  getSpeed()    { return speed;      }
	public Boolean  isSelected()  { return isSelected; }
	public Double   getTargetCoordX()  { return targetCoordX;  }
	public Double   getTargetCoordY()  { return targetCoordY;  }
	public TargetQuarter getTargetQuarter() { return targetQuarter; }
	public Double   getTargetYtoX()    { return targetYtoX;    }
	public Integer  getHealthPoints()  { return healthPoints;  }
	public Integer  getAttack()        { return attack;        }
	public Integer  getAttackRadius()  { return attackRadius;  }
	public Boolean  isMoving()         { return isMoving;      }
	public Boolean  isDead()           { return isDead;        }
	public Shape    getShape()         { return shape;         }

	//======================================================================================
	// Setters
	//======================================================================================
	public void setUnitType(   UnitType unitType   ) { this.unitType   = unitType;   }
	public void setName(       String   name       ) { this.name       = name;       }
	public void setColor(      Color    color      ) { this.color      = color;      }
	public void setCoordX(     Double   coordX     ) { this.coordX     = coordX;     }
	public void setCoordY(     Double   coordY     ) { this.coordY     = coordY;     }
//	public void setWidth(      Integer  width      ) { this.width      = width;      }
//	public void setHeight(     Integer  height     ) { this.height     = height;     }
	public void setSpeed(      Integer  speed      ) { this.speed      = speed;      }
	public void setIsSelected( Boolean  isSelected ) { this.isSelected = isSelected; }
	public void setTargetCoordX(  Double  targetCoordX  ) { this.targetCoordX  = targetCoordX;  }
	public void setTargetCoordY(  Double  targetCoordY  ) { this.targetCoordY  = targetCoordY;  }
	public void setTargetQuarter( TargetQuarter targetQuarter ) { this.targetQuarter = targetQuarter; }
	public void setTargetYtoX(    Double  targetYtoX    ) { this.targetYtoX    = targetYtoX;    }
	public void setHealthPoints(  Integer healthPoints  ) { this.healthPoints  = healthPoints;  }
	public void setAttack(        Integer attack        ) { this.attack        = attack;        }
	public void setAttackRadius(  Integer attackRadius  ) { this.attackRadius  = attackRadius;  }
	public void setIsMoving(      Boolean isMoving      ) { this.isMoving      = isMoving;      }
	public void setShape(         Shape   shape         ) { this.shape         = shape;         }

	//======================================================================================
	public void setIsDead( Boolean isDead )
	{
		this.isDead = isDead;

		if ( isDead )
		{
			color = Color.BLACK;
			isMoving = false;
			healthPoints = 0;
		}
	}

	//======================================================================================
	// Устанавливаем анимацию перемещения объекта в зависимости от положения точки назначения
	//======================================================================================
	private void setMoveAnimation()
	{
		System.out.println(" + GameClient::Unit::setMoveAnimation()");
		System.out.println(" + GameClient::Unit::setMoveAnimation() --- YtoX: " + getTargetYtoX() );

		int[] animation = new int[9];
		MovingDirection movingDirection;

		if ( getTargetQuarter().equals( TargetQuarter.II_Quarter ) )
		{
			System.out.println(" + GameClient::Unit::setMoveAnimation() --- II_Quarter" );
			if      ( getTargetYtoX() > 0                                  && getTargetYtoX() < Math.tan( Math.toRadians(11.25) ) ) { movingDirection = MovingDirection.LEFT; animation = new int[]{ 5, 15, 25, 35, 45, 55, 65, 75, 85 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians(11.25) ) && getTargetYtoX() < Math.tan( Math.toRadians(33.75) ) ) { animation = new int[]{ 6, 16, 26, 36, 46, 56, 66, 76, 86 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians(33.75) ) && getTargetYtoX() < Math.tan( Math.toRadians(56.25) ) ) { animation = new int[]{ 7, 17, 27, 37, 47, 57, 67, 77, 87 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians(56.25) ) && getTargetYtoX() < Math.tan( Math.toRadians(78.75) ) ) { animation = new int[]{ 8, 18, 28, 38, 48, 58, 68, 78, 88 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians(78.75) ) && getTargetYtoX() < Math.tan( Math.toRadians( 90 - Double.MIN_VALUE ) ) ) { animation = new int[]{ 9, 19, 29, 39, 49, 59, 69, 79, 89 }; }
		}
		else if ( getTargetQuarter().equals( TargetQuarter.I_Quarter ) )
		{
			double addition = 90D;
			System.out.println(" + GameClient::Unit::setMoveAnimation() --- I_Quarter " + Math.tan( Math.toRadians( addition + Double.MIN_VALUE ) ) );
			if      ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + Double.MIN_VALUE ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { animation = new int[]{ 160, 170, 180, 190, 200, 210, 220, 230, 240 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 11.25 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { animation = new int[]{ 161, 171, 181, 191, 201, 211, 221, 231, 241 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 33.75 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { animation = new int[]{ 162, 172, 182, 192, 202, 212, 222, 232, 242 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 56.25 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { animation = new int[]{ 163, 173, 183, 193, 203, 213, 223, 233, 243 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 78.75 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 90 - Double.MIN_VALUE ) ) ) { animation = new int[]{ 163, 174, 184, 194, 204, 214, 224, 234, 244 }; }
		}
		else if ( getTargetQuarter().equals( TargetQuarter.IV_Quarter ) )
		{
			double addition = 180D;
			System.out.println(" + GameClient::Unit::setMoveAnimation() --- IV_Quarter" );
			if      ( getTargetYtoX() >= Math.tan( Math.toRadians( addition + Double.MIN_VALUE ) ) && getTargetYtoX() < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { animation = new int[]{ 165, 175, 185, 195, 205, 215, 225, 235, 245 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians( addition + 11.25 ) ) && getTargetYtoX() < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { animation = new int[]{ 166, 176, 186, 196, 206, 216, 226, 236, 246 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians( addition + 33.75 ) ) && getTargetYtoX() < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { animation = new int[]{ 167, 177, 187, 197, 207, 217, 227, 237, 247 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians( addition + 56.25 ) ) && getTargetYtoX() < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { animation = new int[]{ 168, 178, 188, 198, 208, 218, 228, 238, 248 }; }
			else if ( getTargetYtoX() >= Math.tan( Math.toRadians( addition + 78.75 ) ) && getTargetYtoX() < Math.tan( Math.toRadians( addition + 90 - Double.MIN_VALUE ) ) ) { animation = new int[]{ 169, 179, 189, 199, 209, 219, 229, 239, 249 }; }
		}
		else if ( getTargetQuarter().equals( TargetQuarter.III_Quarter ) )
		{
			double addition = 270D;
			System.out.println(" + GameClient::Unit::setMoveAnimation() --- III_Quarter" );
			if      ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + Double.MIN_VALUE ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 11.25 ) ) ) { animation = new int[]{ 0, 10, 20, 30, 40, 50, 60, 70, 80 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 11.25 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 33.75 ) ) ) { animation = new int[]{ 1, 11, 21, 31, 41, 51, 61, 71, 81 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 33.75 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 56.25 ) ) ) { animation = new int[]{ 2, 12, 22, 32, 42, 52, 62, 72, 82 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 56.25 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 78.75 ) ) ) { animation = new int[]{ 3, 13, 23, 33, 43, 53, 63, 73, 83 }; }
			else if ( -getTargetYtoX() >= Math.tan( Math.toRadians( addition + 78.75 ) ) && -getTargetYtoX() < Math.tan( Math.toRadians( addition + 90 - Double.MIN_VALUE ) ) ) { animation = new int[]{ 4, 14, 24, 34, 44, 54, 64, 74, 84 }; }
		}

		this.setAnimationFrame( animation );
	}
	
	//======================================================================================
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	//======================================================================================
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj  ) return true;
		if ( obj  == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		Unit other = (Unit) obj;
		if ( id == null )
		{
			if ( other.id != null ) return false;
		}
		else if ( !id.equals( other.id ) ) return false;
		return true;
	}
}
