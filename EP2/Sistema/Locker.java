public class Locker {

	boolean block = false; // 1 = está bloqueado, 2 - não está bloqueado
	Thread processoBloqueando = null;
	int quantidadeBloqueados = 0;

    public synchronized void desblock() {
		if (Thread.currentThread() == this.processoBloqueando) {
			quantidadeBloqueados--;

			if (quantidadeBloqueados == 0) {
				block = false;
				notify();
			}
		}
	}

	public synchronized void block() throws InterruptedException {
		Thread thread = Thread.currentThread();
		while (block && processoBloqueando != thread) {
			wait();
		}
		block = true;
		quantidadeBloqueados++;
		processoBloqueando = thread;
	}
	
}
