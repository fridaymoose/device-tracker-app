package ovh.kg4.devicetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "device_info")
@Data
public class DeviceInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String technology;
  @Column(name = "bands_2g", nullable = false)
  private String bands2G;
  @Column(name = "bands_3g", nullable = false)
  private String bands3G;
  @Column(name = "bands_4g", nullable = false)
  private String bands4G;
  @OneToOne
  private Device device;
}