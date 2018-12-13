package com.compomics.tessa.controllers;

import com.compomics.tessa.dataTransferObjects.Peptide;
import com.compomics.tessa.dataTransferObjects.Protein;
import com.compomics.tessa.dataTransferObjects.ProteinDTO;
import com.compomics.tessa.dataTransferObjects.VisualisationObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by demet on 12/4/2017.
 */
@Controller
public class IndexController {

    private final String protein_GTG = "GTG";
    private final String protein_JL = "JL";

    private ResourceLoader resourceLoader;

    // color code map, k:sequence, v:color code of the peptide
    private Map<String, String> colorCodeMap = new HashMap<>();

    private List<String> colors = Arrays.asList("#4ECDC4",
            "#013243",
            "#FABE58",
            "#F9690E",
            "#16A085",
            "#96281B",
            "#f0b8ab",
            "#674172",
            "#C06C84",
            "#641E16",
            "#C39BD3",
            "#1A5276",
            "#7D6608",
            "#7A9EB1",
            "#8AAE92",
            "#B30753",
            "#336699",
            "#663300",
            "#FFBAD2",
            "#B378D3",
            "#129793",
            "#99CC00",
            "#3A5F0B",
            "#FF717E",
            "#660033",
            "#ff06ed",
            "#6600CC",
            "#E8D0A9",
            "#AEB05D",
            "#669999",
            "#097054",
            "#FE4902",
            "#9999CC",
            "#CCCC00",
            "#BAA378",
            "#00628B",
            "#AD235E",
            "#41924B",
            "#79BEDB",
            "#33FF33");

    private int colorIndex =0;

    public IndexController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping("/index")
    String index(@RequestParam(value="alleles",defaultValue = "") String alleles,
            @RequestParam(value="region",defaultValue = "") String region,
                 @RequestParam(value="protein",defaultValue = "") String protein, Model model){
        Map<String,Object> attr = new HashMap<>();
        List<VisualisationObject> visualisationObjects = new ArrayList<>();
        List<String> regions =Arrays.asList( region.split(","));
        List<String> proteins =Arrays.asList( protein.split(","));
        int index =0;
        for(String p : proteins){
            for(String r: regions){
                if(!p.equals("") && !r.equals("")){
                    // read GTG fasta file
                    Protein proteinGTG = parseFastaFile(alleles, p + "_sequence_" + protein_GTG);
                    ProteinDTO proteinDTOGTG = createProteinDTO(alleles, protein_GTG, r, proteinGTG);

                    // read JL fasta file
                    Protein proteinJL = parseFastaFile(alleles, p + "_sequence_" +protein_JL);
                    ProteinDTO proteinDTOJL = createProteinDTO(alleles, protein_JL, r, proteinJL);

                    visualisationObjects.add(new VisualisationObject(index, p,r, proteinDTOGTG, proteinDTOJL));
                    colorIndex =0;
                    colorCodeMap.clear();
                    index++;
                }
            }
        }

        attr.put("visualisationObjects", new Gson().toJson(visualisationObjects));

        model.addAllAttributes(attr);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(visualisationObjects);
        System.out.println(json);
        return "index";

    }

    public ProteinDTO createProteinDTO(String alleles, String proteinType, String region, Protein protein){
        System.out.println(proteinType + " : " + protein.getSequence().length());
        List<Peptide> peptides = new ArrayList<>();
        List<Peptide> peptidesAll = new ArrayList<>();
        for(String seq : parsePeptideFile(alleles, protein.getName().split("_")[0], region, proteinType)) {
            if((alleles.equals("1") && seq.length() > 8) || (alleles.equals("2") && seq.length() > 10)){
                int start = protein.getSequence().indexOf(seq) +1;
                int end = start + seq.length() -1;
                float percentage = (float)( seq.length() * 100 )/(float) protein.getSequence().length();
                String colorCode = "";
                if(colorCodeMap.containsKey(seq)){
                    colorCode = colorCodeMap.get(seq);
                }else{
                    colorCode = randomColorGenerator();
                    colorCodeMap.put(seq, colorCode);
                    colorIndex++;
                }

                peptides.add(new Peptide(start, end, seq, percentage, colorCode));
            }
        }

        Collections.sort(peptides, new Comparator<Peptide>() {
            @Override
            public int compare(Peptide o1, Peptide o2) {
                return new Integer(o1.getStart()).compareTo(o2.getStart());
            }
        });
        if(!peptides.isEmpty()){
            peptidesAll.add(peptides.get(0));
            if(peptides.get(0).getStart()>0){
                float percentage =((float) (peptides.get(0).getStart() * 100) )/ (float)protein.getSequence().length();
                peptidesAll.add(new Peptide(0, peptides.get(0).getStart()-1, "no peptides found", percentage, "#C0C0C0"));
            }

            for (int i=1; i<peptides.size(); i++) {
                if(peptides.get(i-1).getEnd() < peptides.get(i).getStart()){
                    float percentage =((float)(( peptides.get(i).getStart() - peptides.get(i-1).getEnd() -1 )* 100 ))/(float)protein.getSequence().length();
                    peptidesAll.add(new Peptide(peptides.get(i-1).getEnd() +1,  peptides.get(i).getStart()-1, "no peptides found", percentage, "#C0C0C0"));

                }
                peptidesAll.add(peptides.get(i));
            }

            if(peptides.get(peptides.size()-1).getEnd()< protein.getSequence().length()-1){
                float percentage =((float)(( protein.getSequence().length()-1 - peptides.get(peptides.size()-1).getEnd())* 100 ))/(float)protein.getSequence().length();
                peptidesAll.add(new Peptide(peptides.get(peptides.size()-1).getEnd()+1,  protein.getSequence().length()-1, "no peptides found", percentage, "#C0C0C0"));
            }
        }

        Collections.sort(peptidesAll, new Comparator<Peptide>() {
            @Override
            public int compare(Peptide o1, Peptide o2) {
                return new Integer(o1.getStart()).compareTo(o2.getStart());
            }
        });

        return new ProteinDTO(protein, peptidesAll);
    }


    public Protein parseFastaFile(String alleles, String proteinName){
        String resourcePath = "classpath:\\static\\data\\MHC"+ alleles + "_all_peptides\\" + proteinName + ".txt";
        Resource resource = resourceLoader.getResource(resourcePath);
        StringBuilder sequenceBuilder = new StringBuilder();
        try {
            InputStream inputStream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            if (inputStream != null) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if(!line.startsWith(">")){
                        sequenceBuilder.append(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        Protein protein = new Protein(proteinName, sequenceBuilder.toString());

        return protein;
    }

    public List<String> parsePeptideFile(String alleles, String protein, String regionName, String proteinType){
        String resourcePath = "classpath:\\static\\data\\MHC"+ alleles + "_all_peptides\\"+protein+"_peptides_all\\found_peptides_all_" + regionName + "_" + proteinType + ".tsv";
        Resource resource = resourceLoader.getResource(resourcePath);
        List<String> sequences = new ArrayList<>();
        try {
            InputStream inputStream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            if (inputStream != null) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if(!line.equals("\t" + protein_GTG) && !line.equals("\t" + protein_JL)){
                        if(line.split("\\t").length >1){
                            sequences.add(line.split("\\t")[1]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sequences;
    }

    public String randomColorGenerator(){
        // create random object - reuse this as often as possible
        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(256*256*256);

        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt);

        // print it
        return colorCode;
    }
}
