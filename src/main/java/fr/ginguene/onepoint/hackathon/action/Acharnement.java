package fr.ginguene.onepoint.hackathon.action;

import fr.ginguene.onepoint.hackathon.Carte;
import fr.ginguene.onepoint.hackathon.Flotte;
import fr.ginguene.onepoint.hackathon.Planete;
import fr.ginguene.onepoint.hackathon.PlaneteStatus;
import fr.ginguene.onepoint.hackathon.Response;
import fr.ginguene.onepoint.hackathon.ordre.EnvoiFlotte;

public class Acharnement extends AbstractStrategie {

	private int destinationId1 = -1;
	private int destinationId2 = -1;

	public Acharnement(boolean isDebug) {
		super(isDebug);
	}

	public Acharnement() {
		super();
	}

	@Override
	public boolean execute(Response response, Planete source, Carte carte, boolean isOptimizingScore) {

		Planete destination = null;

		if (destinationId1 > 0) {
			destination = carte.getPlanete(destinationId1);

			boolean bombeEnApproche = false;
			for (Flotte flotte : carte.getFlottes(PlaneteStatus.Amie, destination)) {
				if (flotte.getVaisseaux() >= destination.getPopulationMax()) {
					bombeEnApproche = true;
				}
			}
			int nbVaisseau = carte.getNbVaisseauInFlotte(PlaneteStatus.Amie, destination);

			if (destination.getStatus() == PlaneteStatus.Amie || bombeEnApproche
					|| nbVaisseau > destination.getPopulationMax() * 3) {
				destination = null;
				destinationId1 = -1;
			}
		}

		if (destination == null) {
			destination = chooseTarget(source, carte);
			destinationId1 = destination.getId();
		}

		int nbVaisseau = source.getPopulation() - carte.getNbVaisseauInFlotte(PlaneteStatus.Ennemie, source, 20) - 10;

		if (nbVaisseau > 3) {
			EnvoiFlotte ordre = new EnvoiFlotte(carte, source, destination, nbVaisseau);
			source.remPopulation(nbVaisseau);
			response.addOrdre(ordre);
			return true;
		}

		return false;
	}

	private Planete chooseTarget(Planete source, Carte carte) {

		for (Planete aPlanete : carte.getPlanetesOrderByDistance(source)) {
			if (aPlanete.getPopulationMax() > 40 && aPlanete.getTauxCroissance() > 1
					&& aPlanete.getStatus() != PlaneteStatus.Amie) {
				return aPlanete;
			}

		}

		// On choisit la planete ennemie la plus proche
		return carte.getEnnemiLaPlusProche(source);

	}

}
