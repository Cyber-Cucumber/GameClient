package ru.torment.shared;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	transient Map< MovingDirection, int[] > map_MovingDirectionAnimation;

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

		shape = new Ellipse2D.Double( getCoordX() - getWidth()/2, getCoordY() - getHeight()/4, getWidth(), getHeight()/2 );
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
				this.setAnimationFrame( getAnimationFrame()[0] );  // Когда объект останавливается выставляем первый фрэйм анимации
				return;
			}

			//-----------------------------------------------------------------------------------------------
			// Устанавливаем анимацию перемещения объекта в зависимости от положения точки назначения
			//-----------------------------------------------------------------------------------------------
			if ( map_MovingDirectionAnimation != null )
			{
				MovingDirection movingDirection = Util.getMovingDirection( getTargetQuarter(), getTargetYtoX() );
				int[] animation = map_MovingDirectionAnimation.get( movingDirection );
				this.setAnimationFrame( animation );
			}
			//-----------------------------------------------------------------------------------------------

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
		Ellipse2D ellipse2d = (Ellipse2D)shape;
		ellipse2d.setFrame( getCoordX() - getWidth()/2, getCoordY() - getHeight()/4, getWidth(), getHeight()/2 );
		g2d.setColor( getColor() );
//		g2d.fill( shape );
		g2d.draw( shape );
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
