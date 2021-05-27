package cat.itb.yapp.adapters;

/**
 * Interfaz creada para la gestión de clics en las listas.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
@FunctionalInterface
public interface RecyclerItemClickListener {
    void onRecyclerItemClick(int position);
}
