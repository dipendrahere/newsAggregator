package code;
import code.clusteringComponent.DBScanClusterer;

import code.clusteringComponent.BatchClusterService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import code.clusteringComponent.DataCleaner;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import code.models.Cluster;
import code.utility.GlobalFunctions;

import code.clusteringComponent.HierarchicalClusterer;
import code.clusteringComponent.IncrementalService;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {



        //PollingService.getInstance().poll();

        IncrementalService.getInstance().start();



//        Article a = new ArticleBuilder("url4")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("tenni coach remain sidelin tenni coach remain sidelin play explor ugliest finish grand slam histori 1 00 van natta jr investig stori behind serena william controversi match naomi osaka 2018 us open final backstori serena vs umpir premier sunday 1 p m et abc 1 00 close peter bodo cover tenni 35 year most recent espn former wta writer year author numer book includ classic court babylon new york time bestsel pete sampra champion mind print hand signal suppos chang game patrick mouratoglou coach serena william lift hand lap palm parallel made poke motion tri convey william lost first set 2018 us open women final need move forward play insid court know happen next chair umpir carlo ramo happen catch subtl gestur issu william code violat coach within moment sensat controversi would rock sport world began unfold wake incid heavi lobbi among other mouratoglou mani believ tenni offici would final embrac form permiss coach hasn happen tenni coach sascha bajin make best emot year 1 relat third consecut year us open allow court coach qualifi junior wheelchair event tournament offici back away plan allow form coach main men women draw although feel strong regard issu goal work collabor tenni govern bodi build consensus move forward chris widmaier usta director communic told espn com consensus issu might long time come court coach grand slam atp tour level alreadi establish wta tour level divis issu tenni much game gridlock demonstr wrong line call probabl cost william us open quarterfin match jennif capriati 2004 impel usta introduc hawk eye electron line call follow year time rush reform stall track one larg power constitu fierc cling idea uniqu lone warrior natur tenni critic featur game sport prohibit coach count wimbledon among stakehold mani high profil individu includ roger feder camp william oppos chang could prevent recurr last year debacl one thing love tenni alon one time want hear anyon tell anyth figur problem solv william deni mouratoglou ever use hand signal said last year final anoth larg constitu outrag rule coach near imposs enforc routin violat often appli unpredict rub purist wrong way remedi allow coach amount throw babi bathwat serena william argu umpir carlo ramo last year us open final al bello getti imag player accept coach violat rule also violat etho irrit player well pundit fan lax applic anti coach rule interview number player suggest illicit coach might much impact match outcom irrit cheat never felt lost match coach player box john isner told espn com one time bit overblown isner feel ever cheat anyon match maintain communic team player box natur thing world other coach behavior matter shade gray black white someon box eager make gestur indic stay low someth unlik mouratoglou signal realli coach meaning sens one thing love tenni alon one time want hear anyon tell anyth figur problem solv serena william almost 7 feet tall said m lift serv return stay low someth know need keep tell need anyway matter look box coach gestur m even look coach realli encourag tenni sandgren rank 72 labor long hard atp challeng tour make remark breakthrough onto main tour 2018 come scorn coach rule seen includ oppon activ counsel entir match sat changeov coach seat right behind never felt cost match sandgren said mayb look like ask go think anyon ever got golden knowledg chang outcom play 0 52 behind serena 2018 us open drama excerpt espn backstori clinton yate explain might spur verbal alterc chair umpir serena william 2018 us open final court coach introduc wta tour twilight year chanda rubin career rubin accustom face rival sometim clandestin coach equal use solv problem eschew court coach felt disadvantag other legitim avail never felt lost coach said box court allow advantag alway think match court coach introduc wta 2008 trial basi 11th year quiet accept addit televis view experi compel coach visit bianca andreescu great run indian well last march produc along pain awkward one innov hasn move needl enough term spectat interest make atp tour major sit take notic hasn game changer sens word tournament allow coach kick appreci differ result produc match impact coach visit obvious coach minut half rubin said enough chang thing big way solut insid enabl viewer listen key element decis allow coach coach mike problemat andi roddick said ok good record tommi robredo know m go play next 10 year would bring coach discuss strategi air serena william deni coach patrick mouratoglou use hand signal match mast irham epa efe rex shutterstock rubin note far one tri quantifi often coach visit alter pattern match add theori coach rule adopt main potenti entertain valu get hood hear player coach talk rubin said espn analyst pam shriver pro coach tournament part doesn think sport keep point rule won enforc guess even coach solv said someon give advic right filter execut still lone thing like pitcher close ninth inning perhap surpris gender play predict role attitud court coach sam querrey recent told espn com m probabl minor wish often possibl rafael nadal clear stand point tenni player pay coach year long call upon re need well theoret anyway nadal receiv frequent code violat fine coach violat uncl coach command nadal guest box contrast tenni channel analyst traci austin two time us open champion said wta court coach rule think taken altogeth think mano mano whatev tool whether emot technic tenni wise versus analyst uncomfort optic creat wta rule martina navratilova told tenni magazin like fact women tour men tour look like women need help men also sight older men pro tour coach men often father player question gesticul lectur young player sit passiv listen stare space put see get contenti bring tear think add sport rubin said take away idea sport full strong independ women battl week week wta model court coach big head start approach perhap signific isn one tri usta secondari event one allow player consult coach seat courtsid side court approach atp experi nextgen final milan exhibit best 21 pros player allow communic coach via headset presum perspir proof one none solut satisfi stakehold unlik adopt soon might turn problem isn worth cost solut")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .setImageUrl("temp")
//                .build();
//
//        List<Article> list = new ArrayList<>();
//        list.add(a);
//        DBConnect.getInstance().insertArticles(list);
        /*
        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship();
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            Article c = (Article) mapElement.getKey();
            System.out.println(c.getUrl() + " " + mapElement.getValue());

        }


//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

//        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        System.out.println(list.size());
//        HierarchicalClusterer<Article> hierarchicalCluster = new HierarchicalClusterer<Article>(0.45);
//        hierarchicalCluster.cluster(list);
//        List<Cluster<Article>> clusters = hierarchicalCluster.cluster(list);
//        try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            Log.error("Unable to dump clusters");
//        }


//        HashMap<String,Integer> hashMap = new HashMap<>();
//        hashMap.put("0044625c8241a4728d35bd2b7402fb60",1);
//        hashMap.put("0049220afeb67fd2e2326b203450649b",5);
//        DBConnect.getInstance().updateClusterIDs(hashMap);

        System.out.println(DBConnect.getInstance().maxClusterId());


         */
    }
}