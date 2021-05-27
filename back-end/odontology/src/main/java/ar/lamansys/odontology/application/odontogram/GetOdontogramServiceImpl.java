package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.OdontogramQuadrantData;
import ar.lamansys.odontology.domain.OdontogramQuadrantStorage;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetOdontogramServiceImpl implements GetOdontogramService {

    private final ToothStorage toothStorage;
    private final OdontogramQuadrantStorage odontogramQuadrantStorage;
    private final Logger LOG;

    public GetOdontogramServiceImpl(
            ToothStorage toothStorage,
            OdontogramQuadrantStorage odontogramQuadrantStorage) {
        this.toothStorage = toothStorage;
        this.odontogramQuadrantStorage = odontogramQuadrantStorage;
        this.LOG = LoggerFactory.getLogger(getClass());
    }

    public List<OdontogramQuadrantBo> run() {
        var teeth = toothStorage.getAll();
        Map<Integer, OdontogramQuadrantBo> quadrantMap = OdontogramQuadrantData.getAsMap();
        teeth.forEach(t -> quadrantMap.get(t.getQuadrantCode()).addTooth(t));
        var result = new ArrayList<>(quadrantMap.values());
        LOG.debug("Output -> {}", result);
        return result;
    }
}