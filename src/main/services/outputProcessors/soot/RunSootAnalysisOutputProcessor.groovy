package services.outputProcessors.soot

import interfaces.OutputProcessor

import java.text.DecimalFormat
import java.text.NumberFormat

import static app.MiningFramework.arguments

/**
 * @requires: that soot-analysis.jar is in the dependencies folder and that
 * FetchBuildsOutputProcessor and GenerateSootInputFilesOutputProcessor were ran
 * @provides: a [outputPath]/data/soot-results.csv file with the results for the soot algorithms ran
 */
class RunSootAnalysisOutputProcessor implements OutputProcessor {

    private final String RESULTS_FILE_PATH = "/data/results-with-build-information.csv"
    private final Long TIMEOUT = 50

    private final SootAnalysisWrapper sootWrapper = new SootAnalysisWrapper("0.2.1-SNAPSHOT")

    private ConflictDetectionAlgorithm[] detectionAlgorithms = [
//            new NonCommutativeConflictDetectionAlgorithm("DF Intra", "svfa-intraprocedural", sootWrapper, TIMEOUT),
//            new NonCommutativeConflictDetectionAlgorithm("DF Inter", "svfa-interprocedural", sootWrapper, TIMEOUT),
//            new ConflictDetectionAlgorithm("Confluence Intra", "dfp-confluence-intraprocedural", sootWrapper, TIMEOUT),
            new ConflictDetectionAlgorithm("Confluence Inter", "dfp-confluence-interprocedural", sootWrapper, TIMEOUT),
//            new ConflictDetectionAlgorithm("OA Intra", "overriding-intraprocedural", sootWrapper, TIMEOUT),
            new ConflictDetectionAlgorithm("OA Inter", "overriding-interprocedural", sootWrapper, TIMEOUT),
//            new NonCommutativeConflictDetectionAlgorithm("DFP-Intra", "dfp-intra", sootWrapper, TIMEOUT),
            new NonCommutativeConflictDetectionAlgorithm("DFP-Inter", "dfp-inter", sootWrapper, TIMEOUT),
//            new NonCommutativeConflictDetectionAlgorithm("CD", "cd", sootWrapper, TIMEOUT),
//            new NonCommutativeConflictDetectionAlgorithm("CDe", "cd-e", sootWrapper, TIMEOUT),
            new NonCommutativeConflictDetectionAlgorithm("PDG", "pdg", sootWrapper, TIMEOUT),
//            new NonCommutativeConflictDetectionAlgorithm("PDG-e", "pdg-e", sootWrapper, TIMEOUT),
//            new ConflictDetectionAlgorithm("Pessimistic Dataflow", "pessimistic-dataflow", sootWrapper, TIMEOUT),
//            new ConflictDetectionAlgorithm("Reachability", "reachability", sootWrapper, TIMEOUT),
    ]

    void setDetectionAlgorithms(List<ConflictDetectionAlgorithm> detectionAlgorithms) {
        this.detectionAlgorithms = detectionAlgorithms
    }

    void configureDetectionAlgorithmsTimeout(int timeout) {
        for (ConflictDetectionAlgorithm algorithm : detectionAlgorithms) {
            algorithm.setTimeout(timeout);
        }
    }

    void processOutput() {
        // check if file generated by FetchBuildsOutputProcessor exists
        println "Executing RunSootAnalysisOutputProcessor"
        executeAnalyses(arguments.getOutputPath())
    }

    void executeAnalyses(String outputPath) {
        File sootResultsFile = createOutputFile(outputPath)

        File resultsWithBuildsFile = new File(outputPath + RESULTS_FILE_PATH)
        if (resultsWithBuildsFile.exists()) {

            List<Scenario> sootScenarios = ScenarioReader.read(outputPath, RESULTS_FILE_PATH);

            for (scenario in sootScenarios) {
                if (scenario.getHasBuild()) {
                    long start = System.currentTimeMillis();
                    println "Running soot scenario ${scenario.toString()}"
                    List<String> results = [];

                    for (ConflictDetectionAlgorithm algorithm : detectionAlgorithms) {
                        String algorithmResult = algorithm.run(scenario);

                        results.add(algorithmResult)
                    }
                    long end = System.currentTimeMillis();

                    NumberFormat formatter = new DecimalFormat("#0.00000");
                    results.add(formatter.format((end - start) / 1000d))
                    sootResultsFile << "${scenario.toString()};${results.join(";")}\n"
                }
            }
        }
    }

    private File createOutputFile(String outputPath) {
        File sootResultsFile = new File(outputPath + "/data/soot-results.csv")

        if (sootResultsFile.exists()) {
            sootResultsFile.delete()
        }

//        sootResultsFile << sootWrapper.getSootAnalysisVersionDisclaimer();
        sootResultsFile << buildCsvHeader();

        return sootResultsFile
    }

    private String buildCsvHeader () {
        StringBuilder resultStringBuilder = new StringBuilder("project;class;method;merge commit");

        for (ConflictDetectionAlgorithm algorithm : detectionAlgorithms) {
            resultStringBuilder.append(";${algorithm.generateHeaderName()}");
        }
        resultStringBuilder.append(";Time")
        resultStringBuilder.append("\n");

        return resultStringBuilder.toString();
    }


}