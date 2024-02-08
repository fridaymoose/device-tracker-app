package ovh.kg4.devicetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "device")
@Data
public class Device {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "model_name", unique = true, nullable = false)
  private String modelName;
  @Column(nullable = false)
  private String brand;

  @OneToOne(mappedBy = "device", fetch = FetchType.LAZY)
  private DeviceInfo deviceInfo;

  @OneToOne(mappedBy = "device", fetch = FetchType.LAZY)
  private Booking booking;
}