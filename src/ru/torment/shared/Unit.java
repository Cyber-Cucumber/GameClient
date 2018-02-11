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
import ru.torment.client.GameFrame;
import ru.torment.client.GameMap;
import ru.torment.client.Util;

public class Unit extends AdvanceSprite implements Serializable
{
	private static final long serialVersionUID = 1L;

	private UUID     id;
	private Owner    owner;     // Владелец юнита
	private Race     race;      // Раса (Люди, Зерги, Протосы)
	private Boolean  isFriend;  // Союзник/враг
	private UnitType unitType;  // Тип юнита (Ультралиск и т.д.)
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
	private Boolean  isAttack;
	private Boolean  isDead;
	private Double   coordX_OnMap;
	private Double   coordY_OnMap;
	private Double   targetCoordX_OnMap;
	private Double   targetCoordY_OnMap;

	private transient Shape shape;
	transient Map< MovingDirection, int[] > map_MovingDirectionAnimation;
	transient Map< MovingDirection, int[] > map_AttackDirectionAnimation;
	transient int[] deathAnimation;

	//======================================================================================
	public Unit( BufferedImage[] bufferedImages, UnitType unitType, String name, Color color, Double coordX, Double coordY, Integer width, Integer height, Integer speed )
	{
		super( bufferedImages, coordX, coordY );

		System.out.println(" + GameClient::Unit::Unit()");

		id = UUID.randomUUID();
		this.owner = Owner.USER_1;
		this.isFriend = true;
		this.unitType = unitType;
		this.name     = name;
		this.color    = color;
//		this.coordX   = coordX;
//		this.coordY   = coordY;
		this.width    = width;
		this.height   = height;
		this.speed    = speed;
		isSelected = false;
		healthPoints = 100;
		attack = 5;
		attackRadius = 50;
		isMoving = false;
		isAttack = false;
		isDead   = false;
		setCoordX( coordX );
		setCoordY( coordY );

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
			if ( getTargetCoordX_OnMap() < getCoordX_OnMap() + 5 &&
				 getTargetCoordX_OnMap() > getCoordX_OnMap() - 5 &&
				 getTargetCoordY_OnMap() < getCoordY_OnMap() + 5 &&
				 getTargetCoordY_OnMap() > getCoordY_OnMap() - 5 )
			{
				stopMoving();
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

		// Если юнит атакует
		if ( isAttack )
		{
//			System.out.println(" + GameClient::Unit::update() --- attacks");
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
	// Взаимодейстия с другими объектами
	//======================================================================================
	public void checkInteraction()
	{
//		System.out.println(" + GameClient::Unit::checkInteraction()");

//		List<Unit> list_NeighborUnits = GameField.getUnitsNearCurrentUnit( this );
//		for ( Unit unit_Neighbor : list_NeighborUnits )
//		{
//			stopMoving();
//		}

		if ( isAttack ) { return; }
		// Проверка досягаемости других объектов относительно текущего
		List<Unit> list_EnemyUnits = GameField.getEnemysInKillZone( this );
		for ( Unit unit_Enemy : list_EnemyUnits )
		{
			stopMoving();
			attackEnemy( unit_Enemy );
			break;
//			unit_Enemy.setHealthPoints( unit_Enemy.getHealthPoints() - getAttack() );
//			if ( unit_Enemy.getHealthPoints() <= 0 )
//			{
//				unit_Enemy.setIsDead(true);
//			}
		}
	}

	//======================================================================================
	// Атака вражеского юнита
	//======================================================================================
	public void attackEnemy( Unit unit_Enemy )
	{
		System.out.println(" + GameClient::Unit::attackEnemy()");

		if ( isAttack ) { return; }

		this.isAttack = true;

		//-----------------------------------------------------------------------------------------------
		// Устанавливаем анимацию атаки объекта в зависимости от положения точки назначения
		//-----------------------------------------------------------------------------------------------
		if ( map_AttackDirectionAnimation != null )
		{
			isMoving = false;
			MovingDirection movingDirection = Util.getMovingDirection( getTargetQuarter(), getTargetYtoX() );
			int[] animation = map_AttackDirectionAnimation.get( movingDirection );
			this.setAnimationFrame( animation );
			this.setAnimationTimer( new Timer(160) );
		}
		//-----------------------------------------------------------------------------------------------

//		unit_Enemy.setHealthPoints( unit_Enemy.getHealthPoints() - getAttack() );
//		if ( unit_Enemy.getHealthPoints() <= 0 )
//		{
//			unit_Enemy.setIsDead(true);
//		}		
	}

	//======================================================================================
	// Получить сводную информацию по объекту
	//======================================================================================
	public String getInfo()
	{
		String unitInfo =
			"Название: " + getName() + "_" + getId() + "\n" +
			" X: " + String.format("%.2f", getCoordX() ) + "\n" +
			" Y: " + String.format("%.2f", getCoordY() ) + "\n" +
			" X (onMap): " + String.format("%.2f", getCoordX_OnMap() ) + "\n" +
			" Y (onMap): " + String.format("%.2f", getCoordY_OnMap() ) + "\n" +
			" Target X: " + String.format("%.2f", getTargetCoordX() ) + "\n" +
			" Target Y: " + String.format("%.2f", getTargetCoordY() ) + "\n" +
			" Target X (onMap): " + String.format("%.2f", getTargetCoordX_OnMap() ) + "\n" +
			" Target Y (onMap): " + String.format("%.2f", getTargetCoordY_OnMap() ) + "\n";
		return unitInfo;
	}

	//======================================================================================
	// Переместить объект в новую точку
	//======================================================================================
	public void moveUnit( int newCoordX, int newCoordY )
	{
		System.out.println(" + GameClient::Unit::moveUnit()");

		if ( !this.isDead() )
		{
			this.setTargetCoordX( Double.valueOf( newCoordX ) );
			this.setTargetCoordY( Double.valueOf( newCoordY ) );

			// Определяем в какой четверти координатной плоскости находится точка назначения (относительно объекта)
			defineTargetQuarter();

//			Double dX = Math.abs( this.getCoordX() - this.getTargetCoordX() );
//			Double dY = Math.abs( this.getCoordY() - this.getTargetCoordY() );
			Double dX = Math.abs( this.getCoordX_OnMap() - this.getTargetCoordX_OnMap() );
			Double dY = Math.abs( this.getCoordY_OnMap() - this.getTargetCoordY_OnMap() );
			this.setTargetYtoX( Double.valueOf(dY/dX) );

			this.setIsMoving(true);

			System.out.println(" + GameClient::Unit::moveUnit() --- MOVE");

			GameFrame.jTextArea.setText( getInfo() );
		}
	}

	//======================================================================================
	// Определяем в какой четверти координатной плоскости находится точка назначения (относительно объекта)
	//======================================================================================
	private void defineTargetQuarter()
	{
		System.out.println(" + GameClient::Unit::defineTargetQuarter()");

		TargetQuarter targetQuarter_Current = null;

		if ( this.getCoordX() < this.getTargetCoordX() )  // Точка назначения справа (I или IV четверть)
		{
			if      ( this.getCoordY() < this.getTargetCoordY() ) { targetQuarter_Current = TargetQuarter.IV_Quarter;      }
			else if ( this.getCoordY() > this.getTargetCoordY() ) { targetQuarter_Current = TargetQuarter.I_Quarter;       }
			else                                                  { targetQuarter_Current = TargetQuarter.On_Y_Axis_Right; }
		}
		else if ( this.getCoordX() > this.getTargetCoordX() )  // Точка назначения слева (II или III четверть)
		{
			if      ( this.getCoordY() < this.getTargetCoordY() ) { targetQuarter_Current = TargetQuarter.III_Quarter;    }
			else if ( this.getCoordY() > this.getTargetCoordY() ) { targetQuarter_Current = TargetQuarter.II_Quarter;     }
			else                                                  { targetQuarter_Current = TargetQuarter.On_Y_Axis_Left; }
		}
		else  // Точка назначения на оси X
		{
			if ( this.getCoordY() < this.getTargetCoordY() ) { targetQuarter_Current = TargetQuarter.On_X_Axis_Down; }
			else                                             { targetQuarter_Current = TargetQuarter.On_X_Axis_Up;   }
		}

		this.setTargetQuarter( targetQuarter_Current );
	}

	//======================================================================================
	// Остановить перемещение
	//======================================================================================
	public void stopMoving()
	{
		System.out.println(" + GameClient::Unit::stopMoving()");

		setTargetCoordX(null);
		setTargetCoordY(null);
		setIsMoving(false);
		this.setAnimationFrame( getAnimationFrame()[0] );  // Когда объект останавливается выставляем первый фрэйм анимации
	}

	//======================================================================================
	// Getters
	//======================================================================================
	public UUID     getId()       { return id;         }
	public Owner    getOwner()    { return owner;      }
	public Race     getRace()     { return race;       }
	public Boolean  isFriend()    { return isFriend;   }
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
	public Double   getCoordX_OnMap()  { return coordX_OnMap;  }
	public Double   getCoordY_OnMap()  { return coordY_OnMap;  }
	public Double   getTargetCoordX_OnMap() { return targetCoordX_OnMap; }
	public Double   getTargetCoordY_OnMap() { return targetCoordY_OnMap; }

	//======================================================================================
	// Setters
	//======================================================================================
	public void setOwner(      Owner    owner      ) { this.owner      = owner;      }
	public void setRace(       Race     race       ) { this.race       = race;       }
	public void setIsFriend(   Boolean  isFriend   ) { this.isFriend   = isFriend;   }
	public void setUnitType(   UnitType unitType   ) { this.unitType   = unitType;   }
	public void setName(       String   name       ) { this.name       = name;       }
	public void setColor(      Color    color      ) { this.color      = color;      }
//	public void setWidth(      Integer  width      ) { this.width      = width;      }
//	public void setHeight(     Integer  height     ) { this.height     = height;     }
	public void setSpeed(      Integer  speed      ) { this.speed      = speed;      }
	public void setIsSelected( Boolean  isSelected ) { this.isSelected = isSelected; }
	public void setTargetQuarter( TargetQuarter targetQuarter ) { this.targetQuarter = targetQuarter; }
	public void setTargetYtoX(    Double  targetYtoX    ) { this.targetYtoX    = targetYtoX;    }
	public void setHealthPoints(  Integer healthPoints  ) { this.healthPoints  = healthPoints;  }
	public void setAttack(        Integer attack        ) { this.attack        = attack;        }
	public void setAttackRadius(  Integer attackRadius  ) { this.attackRadius  = attackRadius;  }
	public void setIsMoving(      Boolean isMoving      ) { this.isMoving      = isMoving;      }
	public void setShape(         Shape   shape         ) { this.shape         = shape;         }
	public void setCoordX_OnMap(  Double  coordX_OnMap  ) { this.coordX_OnMap  = coordX_OnMap;  }
	public void setCoordY_OnMap(  Double  coordY_OnMap  ) { this.coordY_OnMap  = coordY_OnMap;  }
	public void setTargetCoordX_OnMap( Double targetCoordX_OnMap ) { this.targetCoordX_OnMap = targetCoordX_OnMap; }
	public void setTargetCoordY_OnMap( Double targetCoordY_OnMap ) { this.targetCoordY_OnMap = targetCoordY_OnMap; }

	public void setCoordX( Double coordX )
	{
		this.coordX = coordX;
		this.setCoordX_OnMap( GameMap.baseGameMapX1 + coordX );
	}

	public void setCoordY( Double coordY )
	{
		this.coordY = coordY;
		this.setCoordY_OnMap( GameMap.baseGameMapY1 + coordY );
	}

	public void setTargetCoordX( Double targetCoordX )
	{
		this.targetCoordX = targetCoordX;
		if ( targetCoordX == null ) { this.setTargetCoordX_OnMap(null); }
		else                        { this.setTargetCoordX_OnMap( GameMap.baseGameMapX1 + targetCoordX ); }
	}

	public void setTargetCoordY( Double targetCoordY )
	{
		this.targetCoordY = targetCoordY;
		if ( targetCoordY == null ) { this.setTargetCoordY_OnMap(null); }
		else                        { this.setTargetCoordY_OnMap( GameMap.baseGameMapY1 + targetCoordY ); }
	}

	//======================================================================================
	public void setIsDead( Boolean isDead )
	{
		this.isDead = isDead;

		if ( isDead )
		{
			color = Color.BLACK;
			isMoving = false;
			healthPoints = 0;
			this.setAnimationFrame( deathAnimation );
			this.setLoopAnim(false);

			GameField.getListDeadUnits().add( this );
			GameField.getListUnits().remove( this );
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
