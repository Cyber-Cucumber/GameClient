package ru.torment.shared;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;

public class Unit implements Serializable
{
	private static final long serialVersionUID = 1L;

	private UUID     id;
	private UnitType unitType;
	private String   name;
	private Color    color;
	private Double   coordX;
	private Double   coordY;
	private Integer  width;
	private Integer  height;
	private Integer  speed;
	private Boolean  isSelected;
	private Double   targetCoordX;
	private Double   targetCoordY;
	private Integer  targetQuarter;
	private Double   targetXtoY;
	private Integer  healthPoints;
	private Integer  attack;
	private Integer  attackRadius;
	private Boolean  isMoving;
	private Boolean  isDead;

	public Unit( UnitType unitType, String name, Color color, Double coordX, Double coordY, Integer width, Integer height, Integer speed )
	{
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
	}

	public UUID     getId()       { return id;         }
	public UnitType getUnitType() { return unitType;   }
	public String   getName()     { return name;       }
	public Color    getColor()    { return color;      }
	public Double   getCoordX()   { return coordX;     }
	public Double   getCoordY()   { return coordY;     }
	public Integer  getWidth()    { return width;      }
	public Integer  getHeight()   { return height;     }
	public Integer  getSpeed()    { return speed;      }
	public Boolean  isSelected()  { return isSelected; }
	public Double   getTargetCoordX()  { return targetCoordX;  }
	public Double   getTargetCoordY()  { return targetCoordY;  }
	public Integer  getTargetQuarter() { return targetQuarter; }
	public Double   getTargetXtoY()    { return targetXtoY;    }
	public Integer  getHealthPoints()  { return healthPoints;  }
	public Integer  getAttack()        { return attack;        }
	public Integer  getAttackRadius()  { return attackRadius;  }
	public Boolean  isMoving()         { return isMoving;      }
	public Boolean  isDead()           { return isDead;        }

	public void setUnitType(   UnitType unitType   ) { this.unitType   = unitType;   }
	public void setName(       String   name       ) { this.name       = name;       }
	public void setColor(      Color    color      ) { this.color      = color;      }
	public void setCoordX(     Double   coordX     ) { this.coordX     = coordX;     }
	public void setCoordY(     Double   coordY     ) { this.coordY     = coordY;     }
	public void setWidth(      Integer  width      ) { this.width      = width;      }
	public void setHeight(     Integer  height     ) { this.height     = height;     }
	public void setSpeed(      Integer  speed      ) { this.speed      = speed;      }
	public void setIsSelected( Boolean  isSelected ) { this.isSelected = isSelected; }
	public void setTargetCoordX(  Double  targetCoordX  ) { this.targetCoordX  = targetCoordX;  }
	public void setTargetCoordY(  Double  targetCoordY  ) { this.targetCoordY  = targetCoordY;  }
	public void setTargetQuarter( Integer targetQuarter ) { this.targetQuarter = targetQuarter; }
	public void setTargetXtoY(    Double  targetXtoY    ) { this.targetXtoY    = targetXtoY;    }
	public void setHealthPoints(  Integer healthPoints  ) { this.healthPoints  = healthPoints;  }
	public void setAttack(        Integer attack        ) { this.attack        = attack;        }
	public void setAttackRadius(  Integer attackRadius  ) { this.attackRadius  = attackRadius;  }
	public void setIsMoving(      Boolean isMoving      ) { this.isMoving      = isMoving;      }

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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

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
