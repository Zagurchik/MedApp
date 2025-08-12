package org.example.medapp.med;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "meds", indexes = @Index(name = "idx_med_name", columnList = "name"))
public class Med {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String name;

    @Lob private String dosages;

    // ===== Почки: структурированные поля по КК и гемодиализу =====
    // ≥60 мл/мин
    private boolean renalGte60NoAdjust;
    private String  renalGte60Init;
    private String  renalGte60Max;

    // 30–60 мл/мин
    private boolean renal30to60NoAdjust;
    private String  renal30to60Init;
    private String  renal30to60Max;

    // 10–30 мл/мин
    private boolean renal10to30NoAdjust;
    private String  renal10to30Init;
    private String  renal10to30Max;

    // <10 мл/мин
    private boolean renalLt10NoAdjust;
    private String  renalLt10Init;
    private String  renalLt10Max;

    // Гемодиализ
    private boolean renalHdNoAdjust;
    private String  renalHdInit;
    private String  renalHdMax;

    // Доп. заметка по почкам (необязательно)
    @Lob private String renal; // можно использовать как «Примечание по почкам»

    // Остальные поля как раньше
    @Lob private String hepatic;   // печень
    @Lob private String elderly;   // пожилые
    @Lob private String extra;     // дополнительно

    private long updatedAt = Instant.now().toEpochMilli();

    // --- getters/setters ниже ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosages() { return dosages; }
    public void setDosages(String dosages) { this.dosages = dosages; }

    public boolean isRenalGte60NoAdjust() { return renalGte60NoAdjust; }
    public void setRenalGte60NoAdjust(boolean v) { this.renalGte60NoAdjust = v; }
    public String getRenalGte60Init() { return renalGte60Init; }
    public void setRenalGte60Init(String v) { this.renalGte60Init = v; }
    public String getRenalGte60Max() { return renalGte60Max; }
    public void setRenalGte60Max(String v) { this.renalGte60Max = v; }

    public boolean isRenal30to60NoAdjust() { return renal30to60NoAdjust; }
    public void setRenal30to60NoAdjust(boolean v) { this.renal30to60NoAdjust = v; }
    public String getRenal30to60Init() { return renal30to60Init; }
    public void setRenal30to60Init(String v) { this.renal30to60Init = v; }
    public String getRenal30to60Max() { return renal30to60Max; }
    public void setRenal30to60Max(String v) { this.renal30to60Max = v; }

    public boolean isRenal10to30NoAdjust() { return renal10to30NoAdjust; }
    public void setRenal10to30NoAdjust(boolean v) { this.renal10to30NoAdjust = v; }
    public String getRenal10to30Init() { return renal10to30Init; }
    public void setRenal10to30Init(String v) { this.renal10to30Init = v; }
    public String getRenal10to30Max() { return renal10to30Max; }
    public void setRenal10to30Max(String v) { this.renal10to30Max = v; }

    public boolean isRenalLt10NoAdjust() { return renalLt10NoAdjust; }
    public void setRenalLt10NoAdjust(boolean v) { this.renalLt10NoAdjust = v; }
    public String getRenalLt10Init() { return renalLt10Init; }
    public void setRenalLt10Init(String v) { this.renalLt10Init = v; }
    public String getRenalLt10Max() { return renalLt10Max; }
    public void setRenalLt10Max(String v) { this.renalLt10Max = v; }

    public boolean isRenalHdNoAdjust() { return renalHdNoAdjust; }
    public void setRenalHdNoAdjust(boolean v) { this.renalHdNoAdjust = v; }
    public String getRenalHdInit() { return renalHdInit; }
    public void setRenalHdInit(String v) { this.renalHdInit = v; }
    public String getRenalHdMax() { return renalHdMax; }
    public void setRenalHdMax(String v) { this.renalHdMax = v; }

    public String getRenal() { return renal; }
    public void setRenal(String renal) { this.renal = renal; }

    public String getHepatic() { return hepatic; }
    public void setHepatic(String hepatic) { this.hepatic = hepatic; }
    public String getElderly() { return elderly; }
    public void setElderly(String elderly) { this.elderly = elderly; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}